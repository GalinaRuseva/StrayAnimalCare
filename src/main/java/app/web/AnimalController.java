package app.web;

import app.animal.model.Animal;
import app.animal.service.AnimalService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.*;
import app.web.mapper.DtoMapper;
import app.web.validators.FileInputValidator;
import app.web.validators.MultipleFilesUploadValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/animals")
@Slf4j
public class AnimalController {

    private final AnimalService animalService;
    private final UserService userService;
    private final FileInputValidator fileInputValidator;
    private final MultipleFilesUploadValidator multipleFilesUploadValidator;


    @Autowired
    public AnimalController(AnimalService animalService, UserService userService, FileInputValidator fileInputValidator, MultipleFilesUploadValidator multipleFilesUploadValidator) {
        this.animalService = animalService;
        this.userService = userService;
        this.fileInputValidator = fileInputValidator;
        this.multipleFilesUploadValidator = multipleFilesUploadValidator;
    }

    @GetMapping("/new")
    public ModelAndView getNewAnimalPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("new-animal");
        modelAndView.addObject("user", user);
        modelAndView.addObject("animalRequest", new AnimalRequest());

        return modelAndView;
    }

    @PostMapping
    public ModelAndView addNewAnimal(@Valid AnimalRequest animalRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("new-animal");
            modelAndView.addObject("user", user);
            modelAndView.addObject("animalRequest", animalRequest);
            return modelAndView;
        }

        Animal animal = animalService.createAnimal(animalRequest, user);

        return new ModelAndView("redirect:/animals/" + animal.getId() + "/profile");
    }

    @PostMapping("/followed/{id}")
    public String followAnimal(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        Animal animal = animalService.getById(id);

        userService.followAnimal(animal, user);

        return "redirect:/home";
    }

    @DeleteMapping("/followed/{id}")
    public String unfollowAnimal(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        Animal animal = animalService.getById(id);

        userService.unfollowAnimalById(animal, user.getId());

        return "redirect:/home";
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getAnimalProfileMenu(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        Animal animal = animalService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("animal-profile");
        modelAndView.addObject("animal", animal);
        modelAndView.addObject("user", user);
        modelAndView.addObject("actionRequest", new ActionRequest());
        modelAndView.addObject("healthRecordRequest", new HealthRecordRequest());
        return modelAndView;
    }

    @GetMapping("/{id}/edit-profile")
    public ModelAndView getEditAnimalPage(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());
        Animal animal = animalService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-animal-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("animal", animal);
        modelAndView.addObject("animalEditRequest", DtoMapper.mapAnimalToAnimalEditRequest(animal));
        modelAndView.addObject("animalEditFilesUploadRequest", new AnimalEditFilesUploadRequest());
        modelAndView.addObject("singleFileUploadRequest", new SingleFileUploadRequest());
        return modelAndView;
    }

    @PutMapping("/{id}/edit-profile")
    public ModelAndView updateAnimalProfile(@PathVariable UUID id,
                                            @Valid AnimalEditRequest animalEditRequest,
                                            BindingResult bindingResult,
                                            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());
        Animal animal = animalService.getById(id);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-animal-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("animal", animal);
            modelAndView.addObject("animalEditRequest", animalEditRequest);
            return modelAndView;
        }

        animalService.editAnimalDetails(id, animalEditRequest);

        return new ModelAndView("redirect:/animals/" + animal.getId() + "/profile");
    }

    @GetMapping
    public ModelAndView getAllAnimalsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());

        List<Animal> allSystemAnimals = animalService.getAllSystemAnimals();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("animals");
        modelAndView.addObject("user", user);
        modelAndView.addObject("allSystemAnimals", allSystemAnimals);

        return modelAndView;
    }

    @DeleteMapping("/{id}/picture/{pictureId}")
    public String deletePictureFromAnimal(@PathVariable UUID id, @PathVariable UUID pictureId) {

        animalService.deletePictureFromAnimal(pictureId);
        return "redirect:/animals/" + id + "/edit-profile";
    }

    @DeleteMapping("/{id}/picture/profile/{pictureId}")
    public String deleteProfilePicture(@PathVariable UUID id, @PathVariable UUID pictureId){

        this.animalService.deleteProfilePicture(id, pictureId);
        return "redirect:/animals/" + id + "/edit-profile";
    }

    @PostMapping("/{id}/picture/profile")
    public ModelAndView addProfilePicture(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Validated SingleFileUploadRequest singleFileUploadRequest, BindingResult bindingResult){

        User user = userService.getById(authenticationMetadata.getUserId());
        Animal animal = animalService.getById(id);

        fileInputValidator.validate(singleFileUploadRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-animal-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("animal", animal);
            modelAndView.addObject("animalEditRequest", DtoMapper.mapAnimalToAnimalEditRequest(animal));
            modelAndView.addObject("animalEditFilesUploadRequest", new AnimalEditFilesUploadRequest());
            modelAndView.addObject("singleFileUploadRequest", singleFileUploadRequest);
            return modelAndView;
        }

        this.animalService.saveProfilePicture(id, singleFileUploadRequest.getFile());
        return new ModelAndView("redirect:/animals/" + id + "/edit-profile");
    }

    @PostMapping("/{id}/picture")
    public ModelAndView addPicturesToAnimal(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid AnimalEditFilesUploadRequest animalEditFilesUploadRequest, BindingResult bindingResult){

        User user = userService.getById(authenticationMetadata.getUserId());
        Animal animal = animalService.getById(id);

        multipleFilesUploadValidator.validate(animalEditFilesUploadRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-animal-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("animal", animal);
            modelAndView.addObject("animalEditRequest", DtoMapper.mapAnimalToAnimalEditRequest(animal));
            modelAndView.addObject("animalEditFilesUploadRequest", animalEditFilesUploadRequest);
            modelAndView.addObject("singleFileUploadRequest", new SingleFileUploadRequest());
            return modelAndView;
        }

        animalService.savePictureToAnimal(id, animalEditFilesUploadRequest.getAnimalPictures());
        return new ModelAndView("redirect:/animals/" + id + "/profile");
    }


}

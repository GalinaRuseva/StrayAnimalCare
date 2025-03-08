package app.web;

import app.animal.model.Animal;
import app.animal.repository.AnimalRepository;
import app.animal.service.AnimalService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.ActionRequest;
import app.web.dto.AnimalEditRequest;
import app.web.dto.AnimalRequest;
import app.web.dto.HealthRecordRequest;
import app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalService animalService;
    private final UserService userService;
    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalController(AnimalService animalService, UserService userService, AnimalRepository animalRepository) {
        this.animalService = animalService;
        this.userService = userService;
        this.animalRepository = animalRepository;
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
    public ModelAndView AddNewAnimal(@Valid AnimalRequest animalRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("new-animal");
            modelAndView.addObject("user", user);
            modelAndView.addObject("animalRequest", animalRequest);
            return modelAndView;
        }

        animalService.createAnimal(animalRequest, user);

        return new ModelAndView("redirect:/home");
    }

    @PostMapping("/followed/{id}")
    public String followAnimal(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        Animal animal = animalService.getById(id);

        userService.followAnimal(animal, user.getId());

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

        return modelAndView;
    }

    @PutMapping("/{id}/edit-profile")
    public ModelAndView updateAnimalProfile(@PathVariable UUID id, @Valid AnimalEditRequest animalEditRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

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

}

package app.web;

import app.animal.model.Animal;
import app.animal.service.AnimalService;
import app.healthRecord.service.HealthRecordService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.ActionRequest;
import app.web.dto.HealthRecordRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/healthRecords")
public class HealthRecordController {

    private final HealthRecordService healthRecordService;
    private final UserService userService;
    private final AnimalService animalService;

    @Autowired
    public HealthRecordController(HealthRecordService healthRecordService, UserService userService, AnimalService animalService) {
        this.healthRecordService = healthRecordService;
        this.userService = userService;
        this.animalService = animalService;
    }


    @PostMapping("/{animalId}/new")
    public ModelAndView createNewHealthRecord(@PathVariable UUID animalId, @Valid HealthRecordRequest healthRecordRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());
        Animal animal = animalService.getById(animalId);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("animal-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("animal", animal);
            modelAndView.addObject("healthRecordRequest", healthRecordRequest);
            modelAndView.addObject("actionRequest", new ActionRequest());
            return modelAndView;
        }

        healthRecordService.createNewHealthRecord(user, animal, healthRecordRequest);

        return new ModelAndView("redirect:/animals/" + animalId + "/profile");
    }
}

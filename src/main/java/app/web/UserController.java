package app.web;

import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.SingleFileUploadRequest;
import app.web.dto.UserEditRequest;
import app.web.mapper.DtoMapper;
import app.web.validators.FileInputValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FileInputValidator fileInputValidator;

    @Autowired
    public UserController(UserService userService, FileInputValidator fileInputValidator) {
        this.userService = userService;
        this.fileInputValidator = fileInputValidator;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsers(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());
        List<User> users = userService.getAllUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("users", users);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getProfileMenu(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User otherUser = userService.getById(id);
        User user = userService.getById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("otherUser", otherUser);

        if(!user.getId().equals(otherUser.getId())) {
            modelAndView.setViewName("user-profile-other-view");
        } else {
            modelAndView.setViewName("user-profile");
        }

        return modelAndView;
    }

    @GetMapping("/{id}/edit-profile")
    public ModelAndView updateUserProfile(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User selectedUser = userService.getById(id);
        User loggedInUser = userService.getById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-user-profile");
        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("selectedUser", selectedUser);
        modelAndView.addObject("userEditRequest", DtoMapper.mapUserToUserEditRequest(selectedUser));
        modelAndView.addObject("singleFileUploadRequest", new SingleFileUploadRequest());
        return modelAndView;
    }

    @PutMapping(path = "/{id}/edit-profile")
    public ModelAndView updateUserProfile(@PathVariable UUID id,  @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, @Valid UserEditRequest userEditRequest, BindingResult bindingResult) {

        User selectedUser = userService.getById(id);
        User loggedInUser = userService.getById(authenticationMetadata.getUserId());

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-user-profile");
            modelAndView.addObject("user", loggedInUser);
            modelAndView.addObject("selectedUser", selectedUser);
            modelAndView.addObject("singleFileUploadRequest", new SingleFileUploadRequest());
            modelAndView.addObject("userEditRequest", userEditRequest);
            return modelAndView;
        }

        userService.editUserDetails(id, userEditRequest);

        return new ModelAndView("redirect:/users/" + selectedUser.getId() + "/profile");
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public String switchUserStatus(@PathVariable UUID id) {

        userService.switchStatus(id);

        return "redirect:/users";
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String switchUserRole(@PathVariable UUID id) {

        userService.switchRole(id);

        return "redirect:/users";
    }

    @DeleteMapping("/{id}/picture/profile/{pictureId}")
    public ModelAndView deleteProfilePicture(@PathVariable UUID id, @PathVariable UUID pictureId){

        this.userService.deleteProfilePicture(id, pictureId);
        return new ModelAndView("redirect:/users/" + id + "/edit-profile");
    }

    @PutMapping("/{id}/picture/profile")
    public ModelAndView addProfilePicture(@PathVariable UUID id, @Validated SingleFileUploadRequest singleFileUploadRequest, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, BindingResult bindingResult){

        User selectedUser = userService.getById(id);
        User loggedInUser = userService.getById(authenticationMetadata.getUserId());

        fileInputValidator.validate(singleFileUploadRequest, bindingResult); // Use the validator

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-user-profile");

            modelAndView.addObject("user", loggedInUser);
            modelAndView.addObject("selectedUser", selectedUser);
            modelAndView.addObject("userEditRequest", DtoMapper.mapUserToUserEditRequest(selectedUser));
            modelAndView.addObject("singleFileUploadRequest", singleFileUploadRequest);
            return modelAndView;
        }

        this.userService.saveProfilePicture(id, singleFileUploadRequest);
        return new ModelAndView("redirect:/users/" + id + "/edit-profile");
    }

}

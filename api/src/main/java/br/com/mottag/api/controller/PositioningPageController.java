package br.com.mottag.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the device positioning visualization page.
 */
@Controller
@RequestMapping("/positioning")
public class PositioningPageController {

    /**
     * Displays the real-time device positioning visualization page.
     *
     * @param model the model to add attributes to
     * @return the name of the Thymeleaf template
     */
    @GetMapping
    public String showPositioningPage(Model model) {
        // Add any initial data needed for the page
        model.addAttribute("pageTitle", "Device Positioning - Real-time Tracking");
        return "positioning";
    }
}

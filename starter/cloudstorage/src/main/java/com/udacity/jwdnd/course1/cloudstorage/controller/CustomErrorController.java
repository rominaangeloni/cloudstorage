package com.udacity.jwdnd.course1.cloudstorage.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("statusCode", statusCode);

            switch (statusCode) {
                case 404:
                    model.addAttribute("errorMessage", "Page not found");
                    break;
                case 500:
                    model.addAttribute("errorMessage", "Internal server error");
                    break;
                case 403:
                    model.addAttribute("errorMessage", "Access denied");
                    break;
                default:
                    model.addAttribute("errorMessage", "Unexpected error");
            }
        }

        return "error";
    }
}


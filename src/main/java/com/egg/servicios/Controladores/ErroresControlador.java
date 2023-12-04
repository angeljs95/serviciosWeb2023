package com.egg.servicios.Controladores;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ErroresControlador implements ErrorController {

    @RequestMapping(value = "/error", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("error");

        String errorMsg = "";

        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "La Pagina que usted busca no existe existe.";
                break;
            }
            case 403: {
                errorMsg = "Usted no puede hacer eso.";
                break;
            }
            case 401: {
                errorMsg = "Usted no tiene ningun poder aqui para hacer eso.";
                break;
            }
            case 404: {
                errorMsg = "La pagina que solicito no fue encontrada.";
                break;
            }
            case 500: {
                errorMsg = "Lo sentimos estamos experimentando un error interno.";
                break;
            }
        }

        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }


    public String getErrorPath() {
        return "/error.html";
    }

}
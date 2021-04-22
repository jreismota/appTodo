/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.apptodo;

import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author josereis
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException>{

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        final Map<String, String> constraintViolations = new HashMap<>();
        
        for (ConstraintViolation cv: exception.getConstraintViolations()) {
            String path = cv.getPropertyPath().toString();
            constraintViolations.put(path, cv.getMessage());
        }
        
        return Response.status(Response.Status.PRECONDITION_FAILED).entity(constraintViolations).build();
    }
}

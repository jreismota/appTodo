package br.edu.unipam.apptodo;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("api/v1")  //domínio/contextPath/api/v1/....  localhost:8081/appTodo/api/v1/
public class JAXRSConfiguration extends Application {

}
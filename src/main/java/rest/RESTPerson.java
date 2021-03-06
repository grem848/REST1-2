package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Person;
import entity.PersonDTO;
import facade.FacadePerson;
import java.util.Date;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import restexception.ExceptionError;

@Path("person")
public class RESTPerson
{

    @Context
    private UriInfo context;
    @Context
    private ServletContext servletContext;
    
    Date date = new Date();

    Gson gson;
    FacadePerson fp = new FacadePerson(Persistence.createEntityManagerFactory("pu"));

    public RESTPerson()
    {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonsJson()
    {
        String json = gson.toJson(fp.getPersons());

        return Response.ok(json).build();
    }

    @Path("{firstName}/{lastName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonJson(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName)
    {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);

        PersonDTO pdto = fp.getPerson(person);

        if (pdto != null)
        {
            return Response.ok(gson.toJson(pdto)).build();
        } else
        {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"status\":\"NOT FOUND\"}").build();
        }

    }

    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPersonJson(String json) // {"firstName":"Rasmus", "lastName":"Friis","phoneNumber":"911"}
    {
        Person person = gson.fromJson(json, Person.class);

        fp.addPerson(person);

        return Response.ok().entity(json).build();

    }

    @DELETE
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePersonJson(String json) // {"id":"4", "firstName":"Rasmus", "lastName":"Friis", "phoneNumber":"911"}
    {
        Person person = gson.fromJson(json, Person.class);
        try
        {
            fp.deletePerson(person);
            return Response.status(Response.Status.ACCEPTED).entity("{\"status\":\"Deleted User: " + person.getFirstName() + " " + person.getLastName() + ", Deletion date: " + date + "\"}").build();

        } catch (Exception e)
        {
            System.out.println(e);
            return Response.status(Response.Status.NOT_FOUND).entity("{\"status\":\"PERSON NOT FOUND\"}").build();
        }

    }

    @PUT
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPersonJson(String json) // {"id":"1", "firstName":"Heinrich", "lastName":"Wottenburger", "phoneNumber":"123"}
    {
        Person person = gson.fromJson(json, Person.class);
        try
        {

            fp.editPerson(person);

            return Response.ok().entity(json).build();

        } catch (Exception e)
        {
            System.out.println(e);
            return Response.status(Response.Status.NOT_FOUND).entity("{\"status\":\"PERSON NOT FOUND\"}").build();
        }
    }

    @Path("{firstName}/{lastName}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(String json, @PathParam("firstName") String firstName, @PathParam("lastName") String lastName)
    {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);

        PersonDTO pdto = fp.getPerson(person);

        if (pdto != null)
        {
            return Response.ok(gson.toJson(pdto)).build();
        } else
        {
            // 0
            //return Response.status(Response.Status.NOT_ACCEPTABLE).entity("{}").build();

            // 1
            //throw new WebApplicationException(Response.status(406).entity("{\"message\":\"There is no person\"}").build());
            //throw new NoPersonException("{\"message\":\"There is no person\"}");
            // 2
            try
            {
                throw new NumberFormatException("Number must be an integer");
            } catch (NumberFormatException e)
            {
                ExceptionError ee = new ExceptionError(e, 500, servletContext.getInitParameter("debug").equals("true"));
                String eejson = gson.toJson(ee);
                return Response.status(500).entity(eejson).build();
            }

            // 3
            //throw new RuntimeException("Some runtime exception occured");                       
        }
    }
}



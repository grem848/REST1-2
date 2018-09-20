package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Person;
import facade.FacadePerson;
import java.util.Date;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("person")
public class RESTPerson
{

    @Context
    private UriInfo context;

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
        Person person = new Person(firstName, lastName, 0);
        
        if (fp.getPerson(person) == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"status\":\"NOT FOUND\"}").build();
        } else
        {
            String json = gson.toJson(fp.getPerson(person));

            return Response.ok(json).build();
        }

    }

    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPersonJson(Person person)
    {

        String jsonBack = gson.toJson(person);

        fp.addPerson(person);

        return Response.ok().entity(jsonBack).build();

    }

    @DELETE
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePersonJson(Person person)
    {
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


    @Path("{id}")
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPersonJson(@PathParam("id") long id, Person person)
    {
        person.setId(id);
        
        String jsonBack = gson.toJson(person);

        fp.editPerson(person);

        return Response.ok().entity(jsonBack).build();

    }

}

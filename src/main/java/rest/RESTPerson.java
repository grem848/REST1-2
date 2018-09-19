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

    @Path("getpersons")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonsJson()
    {
        String json = gson.toJson(fp.getPersons());

        return Response.ok(json).build();
    }

    @Path("getperson")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonJson(@QueryParam("id") long id)
    {
        if (fp.getPerson(id) == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"status\":\"NOT FOUND\"}").build();
        } else
        {
            String json = gson.toJson(fp.getPerson(id));

            return Response.ok(json).build();
        }

    }

    @Path("addperson")
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPersonJson(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName, @DefaultValue("112") @QueryParam("phoneNumber") int phone)
    {
        Person person = new Person(firstName, lastName, phone);

        String jsonBack = gson.toJson(person);

        fp.addPerson(person);

        return Response.ok().entity(jsonBack).build();

    }

    @DELETE
    @Path("deleteperson")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePersonJson(@QueryParam("id") long id)
    {

        try
        {
            Person p1 = fp.getPerson(id);
            fp.deletePerson(id);
            return Response.status(Response.Status.ACCEPTED).entity("{\"status\":\"Deleted User: " + p1.getFirstName() + " " + p1.getLastName() + ", Deletion date: " + date + "\"}").build();

        } catch (Exception e)
        {
            System.out.println(e);
            return Response.status(Response.Status.NOT_FOUND).entity("{\"status\":\"PERSON NOT FOUND\"}").build();
        }

    }

    @Path("editperson/{id}")
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPersonJson(@PathParam("id") long id, @QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName, @DefaultValue("112") @QueryParam("phoneNumber") int phone)
    {
        Person person = new Person(firstName, lastName, phone);
        person.setId(id);
        
        String jsonBack = gson.toJson(person);

        fp.editPerson(person);

        return Response.ok().entity(jsonBack).build();

    }

}

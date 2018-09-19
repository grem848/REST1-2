
package converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Person;
import java.util.List;


public class JSONConverter
{

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Person getPersonFromJson(String js)
    {
        Person person = gson.fromJson(js, Person.class);

        return person;
    }

    public String getJSONFromPerson(Person p)
    {
        return gson.toJson(p);
    }

    public String getJSONFromPersons(List<Person> persons)
    {
        return gson.toJson(persons);
    }
}

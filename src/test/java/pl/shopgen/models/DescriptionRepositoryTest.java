package pl.shopgen.models;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.shopgen.repositories.DescriptionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DescriptionRepositoryTest extends SimpleMongoRepositoryTest <Description, DescriptionRepository> {

    @Override
    public Description getObject() {
        Description description = new Description();
        description.setName("nazwa");
        description.setValue("opis");

        return description;
    }

    @Override
    public List<Description> getObjects() {
        List<Description> descriptions = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> {
            Description description = getObject();
            description.setValue(description.getValue() + i);
            descriptions.add(description);
        });

        return descriptions;
    }

    @Override
    public Description getChangedObject(Description object) {
        Description newDescription = new Description(object);
        newDescription.setValue(newDescription.getValue() + "nowa");
        return newDescription;
    }
}
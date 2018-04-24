package pl.shopgen.models;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public abstract class SimpleMongoRepositoryTest<T extends SimpleObject, R extends MongoRepository<T, String>> {

    private T savedObject;

    private List<T> savedObjects;

    @Autowired
    private R repository;

    public abstract T getObject();
    public abstract List<T> getObjects();
    public abstract T getChangedObject(T object);

    @Test
    public void saveAndFindObject() {
        saveObject();
        T foundObject = repository.findById(savedObject.getId()).orElse(null);
        assertEquals(savedObject, foundObject);
    }

    private void saveObject() {
        savedObject = repository.save(getObject());
    }

    @Test
    public void deleteObject() {
        saveObject();
        Optional<T> foundedObject;
        repository.deleteById(savedObject.getId());
        foundedObject = repository.findById(savedObject.getId());
        assertFalse(foundedObject.isPresent());
    }

    @Test
    public void update() {
        saveObject();
        T updatedObject;
        T changedObject = getChangedObject(savedObject);

        updatedObject =  repository.save(changedObject);

        assertEquals(changedObject, updatedObject);
    }

    @Test
    public void findAll() {
        saveManyObjects();

        Iterable<T> foundedObjects;
        List<T> foundedObjectList = new ArrayList<>();

        foundedObjects = repository.findAllById(savedObjects.stream()
                                                                     .map(T::getId)
                                                                     .collect(Collectors.toList()));

        foundedObjects.forEach(foundedObjectList::add);
        assertEquals(savedObjects, foundedObjectList);
    }

    @Test
    public void deleteAll() {
        saveManyObjects();
        Iterable<T> foundedObjects;

        repository.deleteAll(savedObjects);
        foundedObjects = repository.findAllById(savedObjects.stream()
                                                                     .map(T::getId)
                                                                     .collect(Collectors.toList()));
        assertFalse(foundedObjects.iterator().hasNext());
    }

    private void saveManyObjects() {
        savedObjects = repository.saveAll(getObjects());
    }
}

package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MainCollections {
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String NAME_3 = "name3";
//    private static final String NAME_4 = "uuid4";

    private static final Resume RESUME1 = new Resume(NAME_1);
    private static final Resume RESUME2 = new Resume(NAME_2);
    private static final Resume RESUME3 = new Resume(NAME_3);
//    private static final Resume resumeOver = new Resume(UUID_4);

    public static void main(String[] args) {

        Collection<Resume> collection = new ArrayList<>();
        collection.add(RESUME1);
        collection.add(RESUME2);
        collection.add(RESUME3);

        for (Resume r : collection) {
//            System.out.println(r);
            if (Objects.equals(r.getUuid(), NAME_1)) {
//                collection.remove(r);
            }
        }

        Iterator<Resume> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resume r = iterator.next();
            System.out.println(r);
            if (Objects.equals(r.getUuid(), NAME_1)) {
                iterator.remove();
            }
        }
        System.out.println(collection);

        Map<String, Resume> map = new HashMap<>();
        map.put(NAME_1, RESUME1);
        map.put(NAME_2, RESUME2);
        map.put(NAME_3, RESUME3);

        for (String uuid : map.keySet()) {
            System.out.println(uuid);
        }

        for (Map.Entry<String, Resume> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }

        List<Resume> resumes = Arrays.asList(RESUME1, RESUME2, RESUME3);
        resumes.remove(1);
        System.out.println(resumes);

    }
}

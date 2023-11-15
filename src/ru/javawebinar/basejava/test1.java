package ru.javawebinar.basejava;

import ru.javawebinar.basejava.storage.Storage;

import java.io.Serializable;

public class test1 {

    private static final Storage storage = Config.get().getStorage();
    public static void main(String[] args) {
//        Resume r = new Resume("vasja");
////        r.addSection(new AbstractSection(SectionType.EDUCATION, "nsu"));
////        List<String> list = Arrays.asList("one ", " two ");
////        System.out.println(list);
////        r.addSection(new AbstractSection(SectionType.EDUCATION, list));
////        r.addSection(new AbstractSection(SectionType.EDUCATION, "2233"));
////        System.out.println(r.toString());
//        try {
//            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("1.txt"));
//            DekR d = new DekR();
//            o.writeObject(d);
//
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        System.out.print(storage.size());

    }

    static class DekR implements Serializable {
        private Ret r;
        private String name;

        public DekR(){
            r = new Ret();
            name = "jgsdj";
        }
    }

    static class Ret implements Serializable{
        private String retName;

        public Ret(){
            retName = "sdfde";
        }
    }
}

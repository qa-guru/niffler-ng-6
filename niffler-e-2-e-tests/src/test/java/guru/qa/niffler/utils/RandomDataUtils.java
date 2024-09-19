package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static String randomUsername(){
        return faker.name().username();
    }
    public static String randomName(){
        return faker.name().firstName();
    }
    public static String randomSurname(){
        return faker.name().lastName();
    }
    public static String randomCategoryName(){
        return faker.name().name();
    }
    public static String randomSentence(int wordsCount){
        String result = "";
        int i = 0;
        while ( i < wordsCount){
            result +=faker.animal().name()+" ";
            i++;
        }
        return result.trim();
    }

}

package utilities;

public class TestDataPayloads {
    ////all payloads suitable for API calls will get there


    public static String payloadMealcardCreation() {
        ///Payload viable to create 3 mealcards on 29-03-2020 day of test account
        return "[\n" +
                "  {\n" +
                "    \"id\": 11111111,\n" +
                "    \"user\": \"\",\n" +
                "    \"dateCreated\": \"2020-03-29\",\n" +
                "    \"number\": 0,\n" +
                "    \"mealTitle\": \"Breakfast\",\n" +
                "    \"namesCalories\": [\n" +
                "      {\n" +
                "        \"name\": \"Egg, whole, boiled or poached\",\n" +
                "        \"energy\": 275\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Honey loaf\",\n" +
                "        \"energy\": 122\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 22222222,\n" +
                "    \"user\": \"\",\n" +
                "    \"dateCreated\": \"2020-03-29\",\n" +
                "    \"number\": 1,\n" +
                "    \"mealTitle\": \"Lunch\",\n" +
                "    \"namesCalories\": [\n" +
                "      {\n" +
                "        \"name\": \"Pancakes, with fruit\",\n" +
                "        \"energy\": 389\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"Strawberries, raw\",\n" +
                "        \"energy\": 110\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 33333333,\n" +
                "    \"user\": \"\",\n" +
                "    \"dateCreated\": \"2020-03-29\",\n" +
                "    \"number\": 2,\n" +
                "    \"mealTitle\": \"Dinner\",\n" +
                "    \"namesCalories\": [\n" +
                "      {\n" +
                "        \"name\": \"ALOHA MIX\",\n" +
                "        \"energy\": 515\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";
    }

    public static String payloadDateCreated() {
        return "{\"dateCreated\":\"2020-03-29\"}";
    }

}

package utilities;

public enum APIEnum {

    UserLoginEndpoint("api/user/login/"),
    GetMealcardsEndpoint("caloriestracker/getMealCards/"),
    SaveMealcardsEndpoint("caloriestracker/postSaveMealCards/");
    private String resource;

    APIEnum(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }

}

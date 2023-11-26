package com.ilyap.yuta.utils;

import com.ilyap.yuta.models.User;

public class RequestUtils {

    private RequestUtils() {
    }

    public static String getUserIdRequest() {
        return "{\"status\": \"OK\", \"user_id\": 1}";
    }

    public static String getUserRequest(int userId) {
        return "{\"login\":\"ribinaka.21\",\"photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Панова\",\"first_name\":\"Ксения\",\"patronymic\":\"Андреевна\",\"age\":\"21 год\",\"biography\":\"Студентка третьего курса университета. Занимаюcь в основном разработкой бэкенда веб-приложений.\",\"phone_number\":\"+7 (901) 278-88-00\",\"e_mail\":\"rybinak95@gmail.com\",\"vk\":\"https://vk.com/bitte_r\",\"faculty\":\"Институт цифровых систем\",\"direction\":\"09.03.04 - Программная инженерия\", \"group\":\"ЦПИ-31\",\"all_projects_count\":2,\"done_projects_count\":0,\"all_tasks_count\":1,\"done_tasks_count\":1,\"teams_count\":2}";
    }

    public static void reloadUserRequest(String password) {
        // TODO
    }

    public static void editUserRequest(User user) {
        // TODO
    }

    public static void deleteUserPhotoRequest(User user) {
        // TODO
    }

    public static void uploadUserPhotoRequest(User user) {
        // TODO
    }

    public static void cropUserPhotoRequest(int imageWidth, int imageHeight, int croppedWidth, int croppedHeight, int offsetX, int offsetY) {
        // TODO
    }

    public static String getTeamsRequest(int userId) {
        return "{\"managed_teams\":[{\"name\":\"Команда Ксюши\",\"leader\":{\"id\":1,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Панова\",\"first_name\":\"Ксения\"},\"members\":[{\"id\":3,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Аристов\",\"first_name\":\"Даниил\"},{\"id\":7,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Матюшанова\",\"first_name\":\"Дарина\"},{\"id\":5,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Пухов\",\"first_name\":\"Илья\"}]}],\"others_teams\":[{\"name\":\"Команда Дани\",\"leader\":{\"id\":3,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Аристов\",\"first_name\":\"Даниил\"},\"members\":[{\"id\":7,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Матюшанова\",\"first_name\":\"Дарина\"},{\"id\":1,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Панова\",\"first_name\":\"Ксения\"},{\"id\":5,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Пухов\",\"first_name\":\"Илья\"}]},{\"name\":\"Команда неДани\",\"leader\":{\"id\":5,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Сальникова\",\"first_name\":\"Екатерина\"},\"members\":[{\"id\":7,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Матюшанова\",\"first_name\":\"Дарина\"},{\"id\":1,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Панова\",\"first_name\":\"Ксения\"},{\"id\":3,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Аристов\",\"first_name\":\"Даниил\"},{\"id\":5,\"cropped_photo\":\"https://github.com/Panovky/YUTA/blob/develop/media/images/cropped-default_user_photo.png?raw=true\",\"last_name\":\"Пухов\",\"first_name\":\"Илья\"}]}]}";
    }
}
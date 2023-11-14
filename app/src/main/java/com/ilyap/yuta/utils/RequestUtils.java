package com.ilyap.yuta.utils;

public class RequestUtils {

    public static String getUserIdRequest() {
        return "{\"status\": \"OK\", \"user_id\": 1}";
    }

    public static String getUserRequest(int userId) {
        return "{\"login\":\"ribinaka.21\",\"photo\":\"/media/images/users_photos/ribinaka.21.jpeg\",\"cropped_photo\":\"/media/images/users_photos/cropped-ribinaka.21.jpeg\",\"last_name\":\"Панова\",\"first_name\":\"Ксения\",\"patronymic\":\"Андреевна\",\"age\":\"21 год\",\"biography\":\"Студентка третьего курса университета. Занимаюcь в основном разработкой бэкенда веб-приложений.\",\"phone_number\":\"+7 (901) 278-88-00\",\"e_mail\":\"rybinak95@gmail.com\",\"vk\":\"https://vk.com/bitte_r\",\"faculty\":\"Институт цифровых систем\",\"direction\":\"09.03.04 - Программная инженерия\", \"group\":\"ЦПИ-31\",\"all_projects_count\":2,\"done_projects_count\":0,\"all_tasks_count\":1,\"done_tasks_count\":1,\"teams_count\":2}";
    }
}

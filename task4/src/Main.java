public class Main {
    public static void main(String[] args) {
        try {
            DataAPI dataAPI = new DataAPI();

            // Пример добавления данных
            DataModel newData = new DataModel(1, "Sample content", true);
            dataAPI.insertData(newData);

            // Пример получения данных (должен быть взят из кэша, так как read-only)
            DataModel retrievedData = dataAPI.getData(1);
            System.out.println("Retrieved data: " + retrievedData);

            // Пример обновления данных
            retrievedData.setContent("Updated content");
            retrievedData.setReadOnly(false); // Теперь данные не будут кэшироваться
            dataAPI.updateData(retrievedData);

            // Пример генерации отчета
            System.out.println("Report:");
            dataAPI.generateReport().forEach(System.out::println);

            // Пример удаления данных
            dataAPI.deleteData(1);

            dataAPI.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
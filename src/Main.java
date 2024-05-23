public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Автоматизировать механизм возврата платежей","Выставить ручку, которая будет отвечать за возврат платежей клиенту", Status.NEW);
        manager.createTask(task1);

        SubTask subTask1 = new SubTask("Доработать модель данных", "Добавить новые поля", Status.NEW);

        Epic epic1 = new Epic("Подключить нового провайдера", "Выставить ручку, которая будет отвечать за возврат платежей клиенту");
        manager.createEpic(epic1);

        subTask1.setEpicId(epic1.getTaskId());
        manager.updateEpic(new Epic("Подключить нового провайдера", "Доработать модель данных", 1));
        manager.updateTask(new Task("Автоматизировать механизм возврата платежей", "Интегрироваться с корбанкингом", 1, Status.IN_PROGRESS));
        manager.createSubTask(subTask1);
        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);

        System.out.println(manager.getAllEpicTasks());
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllTasks());

        System.out.println(manager.getEpicById(1));
        System.out.println(manager.getTaskById(1));

        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();
        manager.deleteAllSubTasks();

        System.out.println(manager.getAllTasks());
    }
}

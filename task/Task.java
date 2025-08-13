import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static String addres = "C:\\Users\\User\\OneDrive\\Desktop\\task\\output";
    public static LocalDate localDate = LocalDate.now();

    public static void main(String[] args) throws IOException {
        reader();
        System.out.println("welcome to task manager!");
        System.out.println("1.add task");
        System.out.println("2.view all tasks");
        System.out.println("3.update a task");
        System.out.println("4.delete task");
        System.out.println("5.Exit");
        Scanner scanner = new Scanner(System.in);
        int choose = scanner.nextInt();
        while (choose != 5) {
            switch (choose) {
                case 1: {
                    addTask();
                }
                break;
                case 2:
                    viewAllTasks();
                    break;
                case 3:
                    updateTask();
                    break;
                case 4: {
                    deleteTask();
                }
            }
            System.out.println("welcome to task manager!");
            System.out.println("1.add task");
            System.out.println("2.view all tasks");
            System.out.println("3.update a task");
            System.out.println("4.delete task");
            System.out.println("5.Exit");
            choose = scanner.nextInt();
        }
    }

    public static List<String[]> reader() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(addres));
        List<String[]> rows = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            rows.add(line.split(","));
        }
        reader.close();
        return rows;
    }

    public static int getId() throws IOException {
        List<String[]> rows = reader();
        int maxId = 0;
        for (String[] row : rows) {
            int id = Integer.parseInt(row[0]);
            if (id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }

    public static void addTask() throws IOException {
        int id = getId();
        BufferedWriter writer = new BufferedWriter(new FileWriter(addres, true));
        System.out.println("enter task title,description,date,status and recurrence");
        Scanner scanner = new Scanner(System.in);
        String title = scanner.next();
        String description = scanner.next();
        String date = scanner.next();
        String status = scanner.next();
        String recurrence = scanner.next();
        writer.write(id + "," + title + "," + description + "," + date + "," + status + "," + recurrence);
        writer.newLine();
        writer.close();
        System.out.println("Task Added !");
    }

    public static void viewAllTasks() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<String[]> rows = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(addres));
        while (bufferedReader.ready()) {
            rows.add(bufferedReader.readLine().split(","));
        }
        for (String[] row : rows) {
            System.out.println("id:" + row[0]);
            LocalDate localDate2 = LocalDate.parse(row[3], dateTimeFormatter);
            if (localDate.isAfter(localDate2))
                System.out.println("title:" + row[1] + "   DELAYED!");
            else System.out.println("title:" + row[1]);
            System.out.println("description:" + row[2]);
            System.out.println("date:" + row[3]);
            System.out.println("status:" + row[4]);
            System.out.println("recurrence:" + row[5]);

        }
    }

    public static void updateTask() throws IOException {
        List<String[]> rows = reader();
        BufferedWriter writer = new BufferedWriter(new FileWriter(addres));
        System.out.println("enter the id of the task that you want to change");
        Scanner scanner = new Scanner(System.in);
        int numberOftask = scanner.nextInt();
        System.out.println("what do you want to update?");
        System.out.println("1.title");
        System.out.println("2.description");
        System.out.println("3.date");
        System.out.println("4.status");
        System.out.println("5.recurrence");
        int theChanger = scanner.nextInt();
        switch (theChanger) {
            case 1: {
                System.out.println("enter the new title");
                String s = scanner.next();
                for (String[] row : rows) {
                    if (Integer.parseInt(row[0]) == numberOftask) {
                        row[1] = s;
                    }
                }
                break;
            }
            case 2: {
                System.out.println("enter the new description");
                String s = scanner.next();
                for (String[] row : rows)
                    if (Integer.parseInt(row[0]) == numberOftask)
                        row[2] = s;
                break;
            }
            case 3: {
                System.out.println("enter the new date");
                String s = scanner.next();
                for (String[] row : rows)
                    if (Integer.parseInt(row[0]) == numberOftask)
                        row[3] = s;
                break;
            }
            case 4: {
                String[] newtask = null;
                System.out.println("enter the new status");
                String s = scanner.next().toLowerCase();
                for (String[] row : rows)
                    if (Integer.parseInt(row[0]) == numberOftask) {
                        row[4] = s;
                        if (s.equals("done")) {
                            String newDate = "";
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            LocalDate localDate2 = LocalDate.parse(row[3], dateTimeFormatter);
                            if (row[5].equals("daily")) {
                                localDate2 = localDate2.plusDays(1);
                                newDate = localDate2.format(dateTimeFormatter).toString();
                            }
                            if (row[5].equals("weekly")) {
                                localDate2 = localDate2.plusWeeks(1);
                                newDate = localDate2.toString();
                            }
                            if (row[5].equals("monthly")) {
                                localDate2 = localDate2.plusMonths(1);
                                newDate = localDate2.toString();
                            }
                            int id = getId();
                            String title = row[1];
                            String description = row[2];
                            String date = newDate;
                            String status = "pending";
                            String recurrence = row[5];
                            newtask = new String[]{String.valueOf(id), title, description, date, status, recurrence};

                        }

                    }
                if (newtask != null) {
                    rows.add(newtask);
                    System.out.println("Task Added !");
                }
                break;
            }
            case 5: {
                System.out.println("enter the new recurrence");
                String s = scanner.next();
                for (String[] row : rows)
                    if (Integer.parseInt(row[0]) == numberOftask)
                        row[5] = s;
                break;
            }
        }
        for (String[] row : rows) {
            writer.write(String.join(",", row));
            writer.newLine();
        }
        writer.close();

    }

    public static void deleteTask() throws IOException {
        List<String[]> rows = reader();
        System.out.println("enter the id of the task that you want to be deleted");
        Scanner scanner = new Scanner(System.in);
        int del = scanner.nextInt();
        BufferedWriter writer = new BufferedWriter(new FileWriter(addres));
        for (String[] row : rows) {
            if (Integer.parseInt(row[0]) != del) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
        writer.close();

    }


}

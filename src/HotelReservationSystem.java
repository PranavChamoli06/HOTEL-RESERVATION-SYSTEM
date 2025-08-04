import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;


public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";

    private static final String user = "yourusername";

    private static final String password = "yourpassword";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        System.out.println("======================================");
        System.out.println("      WELCOME TO HOTEL RESERVATION    ");
        System.out.println("======================================");
        System.out.println("     Developed by Pranav Chamoli 💻   ");
        System.out.println("      Java | JDBC | MySQL Backend     ");
        System.out.println("======================================");

        try{
            Connection conn = DriverManager.getConnection(url,user,password);
            while(true){
                System.out.println();
                Scanner scn = new Scanner(System.in);
                System.out.println("Available Options: ");
                System.out.println("1. Reserve a Room");
                System.out.println("2. View Reservation");
                System.out.println("3. Update Reservation");
                System.out.println("4. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println("Your Choice: ");
                int option = scn.nextInt();
                switch(option){
                    case 1:
                        reserveRoom(conn, scn);
                        break;
                    case 2:
                        viewReservations(conn);
                        break;
                    case 3:
                        updateReservation(conn, scn);
                        break;
                    case 4:
                        deleteReservation(conn,scn);
                        break;
                    case 0:
                        exit();
                        scn.close();
                        return;
                    default:
                        System.out.println("Invalid option. Try again!!!!");
                }
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection conn, Scanner scn){
       try{
           System.out.println("Enter guest name: ");
           String guestName = scn.next();
           scn.nextLine();
           System.out.println("Enter room number: ");
           int roomNumber = scn.nextInt();
           System.out.println("Enter contact number: ");
           String contactNumber = scn.next();

           String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                   "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

           try(Statement stmt = conn.createStatement()){
               int affected = stmt.executeUpdate(sql);

               if(affected > 0){
                   System.out.println("Reservation Successfull");
               } else{
                   System.out.println("Reservation Unsuccessfull");
               }
           }
       } catch (SQLException e){
           e.printStackTrace();
       }
        System.out.println("--------------------------------------------------");
    }

    private static void viewReservations(Connection conn) throws SQLException{
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number," +
                "reservation_date FROM reservations";

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }
    }

    private static void updateReservation(Connection conn, Scanner scn){
        try{
            System.out.println("Enter reservation id to update: ");
            int reservationId = scn.nextInt();
            scn.nextLine();

            if (!reservationExists(conn, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.println("Enter new guest name: ");
            String newGuestName = scn.nextLine();
            System.out.println("Enter new room number: ");
            int newRoomNumber = scn.nextInt();
            System.out.println("Enter new contact number: ");
            String newContactNumber = scn.next();


            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try(Statement stmt = conn.createStatement()){
                int affected = stmt.executeUpdate(sql);
                if(affected > 0){
                    System.out.println("Reservation Updated Successfully");
                } else{
                    System.out.println("Reservation Updated Unsuccessfully");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------------");
    }

    private static void deleteReservation(Connection conn, Scanner scn){
        try{
            System.out.println("Enter the reservation id to delete: ");
            int reservationId = scn.nextInt();

            if(!reservationExists(conn, reservationId)){
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;
            try(Statement stmt = conn.createStatement()){
                int affected = stmt.executeUpdate(sql);

                if(affected > 0){
                    System.out.println("Reservation Deleted Successfully");
                } else{
                    System.out.println("Reservation Deletion Failed");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------------");
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }

    public static void exit() throws InterruptedException{
        System.out.print("Exiting System");
        int i = 5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("THANK YOU FOR CHOOSING OUR HOTEL RESERVATION SYSTEM :)");
    }
}

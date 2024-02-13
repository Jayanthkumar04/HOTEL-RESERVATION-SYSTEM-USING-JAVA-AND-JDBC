package hotelreservations;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Scanner;

public class HOTEL_RESERVATION_SYSTEM {
	private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String userName = "root";
    private static final String password = "Jayanth@04";
    
    
	public static void main(String[] args) {

		try
		{
		Connection conn = DriverManager.getConnection(url,userName,password);
		
		while(true)
		{
			
			System.out.println("HOTEL MANAGEMENT SYSTEM");
			Scanner sc = new Scanner(System.in);
	        System.out.println("1. Reserve a room");
	        System.out.println("2. View Reservations");
	        System.out.println("3. Get room number");
	        System.out.println("4. Update reservations");
	        System.out.println("5. Delete Reservations");
	        System.out.println("0. Exit");
	        
	        System.out.println("enter an option");
	        
	        int choice = sc.nextInt();
	
	        switch(choice)
	        {
	        case 1:reserveRoom(conn,sc);
	               break;
	        case 2:viewReservations(conn,sc);	
                   break;
	        case 3:getRoomNumber(conn,sc);
	               break;
	        case 4:updateReservations(conn,sc);
	               break;
	        case 5:deleteReservations(conn,sc);
	               break;
	        case 0:System.exit(0);
	        
	        default:System.out.println("choose valid option");
	        }
	        
	        
		}
		
		
		
		}
		catch(Exception e)
		{
		      e.printStackTrace();
		}
		
		

	}
	
	private static void reserveRoom(Connection conn,Scanner sc)
	{
		System.out.println("enter guest name: ");
		String name=sc.next();
		sc.nextLine();
		
		System.out.println("enter room no: ");
		int room=sc.nextInt();
		sc.nextLine();
		
		
		System.out.println("enter guest phno: ");
		String phno=sc.nextLine();
		
		
		String query = "insert into reservations (guest_name,room_numb,contact_num) values(?,?,?)";
		
		try
		{
		PreparedStatement stm = conn.prepareStatement(query);
		
		stm.setString(1,name);
		stm.setInt(2, room);
		stm.setString(3,phno);
		
		stm.execute();
		
		System.out.println("1 row inserted successfully");
		
		System.out.println();
		System.out.println();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public static void viewReservations(Connection conn,Scanner sc)
	{
		
		String query ="select * from reservations";
		
		try
		{
			   Statement stm = conn.createStatement();
			
			   ResultSet rs=  stm.executeQuery(query);	
		
			   System.out.println("Reservation id\t\tGuestName\t\tRoomNo\t\tPhone no\t\t\t\tTime of arrival");
			   while(rs.next())
			   {
				   System.out.print(rs.getString(1)+"\t\t\t\t");
				   System.out.print(rs.getString(2)+"\t\t");
				   System.out.print(rs.getString(3)+"\t\t");
				   System.out.print(rs.getString(4)+"\t\t");
				   System.out.print(rs.getString(5)+"\t\t");
				   
				   System.out.println();
			   }
			   System.out.println();
		
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void getRoomNumber(Connection conn,Scanner sc) throws SQLException
	{
		System.out.println("enter the guest name");
		String name=sc.next();
		
		System.out.println("enter reservation id");
		int id=sc.nextInt();
		sc.nextLine();
		
		
		String query ="select room_numb from reservations where guest_name=? and reservation_id=?";
		
		PreparedStatement ps = conn.prepareStatement(query);
		
		ps.setString(1, name);
		ps.setInt(2, id);
		
		
		ResultSet rs2 = ps.executeQuery();
		
		if(rs2.next())
		{
			
			System.out.println("room number is "+rs2.getInt("room_numb"));
		}
		else
		{
			System.out.println("no guest exists in the database.......");
		}
		
		System.out.println();
		
		
			}

	public static void updateReservations(Connection conn,Scanner sc)
	{
		System.out.println("enter reservation id: ");
		int id=sc.nextInt();
		sc.nextLine();
		System.out.println("enter guest name: ");
		String name=sc.next();
		sc.nextLine();
		

        if (!reservationExists(conn, id)) {
            System.out.println("Reservation not found for the given ID.");
            return;
        }

        System.out.print("Enter new guest name: ");
        String newGuestName = sc.nextLine();
        System.out.print("Enter new room number: ");
        int newRoomNumber = sc.nextInt();
        System.out.print("Enter new contact number: ");
        String newContactNumber = sc.next();
		
		 String sql = "UPDATE reservations SET guest_name = '" +newGuestName+ "', " +
                 "room_numb = " + newRoomNumber + ", " +
                 "contact_num = '" + newContactNumber + "' " +
                 "WHERE reservation_id = " + id;

         try (Statement statement = conn.createStatement()) {
             int affectedRows = statement.executeUpdate(sql);

             if (affectedRows > 0) {
                 System.out.println("Reservation updated successfully!");
             } else {
                 System.out.println("Reservation update failed.");
             }
         }
      catch (Exception e) {
         e.printStackTrace();
     }
		
	}
	
	private static boolean reservationExists(Connection conn, int id) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                return resultSet.next(); // If there's a result, the reservation exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle database errors as needed
        }
    }
	
	private static void deleteReservations(Connection connection, Scanner scanner) {
	        try {
	            System.out.print("Enter reservation ID to delete: ");
	            int reservationId = scanner.nextInt();

	            if (!reservationExists(connection, reservationId)) {
	                System.out.println("Reservation not found for the given ID.");
	                return;
	            }

	            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

	            try (Statement statement = connection.createStatement()) {
	                int affectedRows = statement.executeUpdate(sql);

	                if (affectedRows > 0) {
	                    System.out.println("Reservation deleted successfully!");
	                } else {
	                    System.out.println("Reservation deletion failed.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }


}

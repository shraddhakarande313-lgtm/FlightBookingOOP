import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class Passenger {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public abstract double getDiscount();
}

class RegularPassenger extends Passenger {
    public RegularPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscount() {
        return 0.0;
    }
}

class StudentPassenger extends Passenger {
    public StudentPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscount() {
        return 0.10;
    }
}

class Flight {
    private String code;
    private String airline;
    private String source;
    private String destination;
    private int availableSeats;
    private double baseFare;

    public Flight(String code, String airline, String source,
                  String destination, int availableSeats,
                  double baseFare) {
        this.code = code;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.availableSeats = availableSeats;
        this.baseFare = baseFare;
    }

    public String getCode() {
        return code;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public boolean bookSeats(int count) {
        if (count <= 0 || count > availableSeats) {
            return false;
        }
        availableSeats -= count;
        return true;
    }

    public void display() {
        System.out.printf(
            "%s | %s | %s -> %s | Seats: %d | Fare: Rs. %.2f%n",
            code, airline, source, destination,
            availableSeats, baseFare
        );
    }

    public String getDetails() {
        return airline + " (" + code + "): " +
               source + " to " + destination;
    }
}

class Booking {
    private static int nextId = 1001;

    private int bookingId;
    private Passenger passenger;
    private Flight flight;
    private int tickets;
    private double totalFare;
    private String bookingDate;

    public Booking(Passenger passenger, Flight flight,
                   int tickets, double totalFare) {
        this.bookingId = nextId++;
        this.passenger = passenger;
        this.flight = flight;
        this.tickets = tickets;
        this.totalFare = totalFare;

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        this.bookingDate = LocalDateTime.now().format(formatter);
    }

    public void display() {
        System.out.println("\n========== BOOKING SUMMARY ==========");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Passenger: " + passenger.getName());
        System.out.println("Age: " + passenger.getAge());
        System.out.println("Passenger Type: " +
                           passenger.getClass().getSimpleName());
        System.out.println("Flight: " + flight.getDetails());
        System.out.println("Tickets: " + tickets);
        System.out.printf("Total Fare: Rs. %.2f%n", totalFare);
        System.out.println("Booking Date: " + bookingDate);
        System.out.println("=====================================");
    }
}

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    private static final List<Flight> flights = new ArrayList<>();
    private static final List<Booking> bookings = new ArrayList<>();

    public static void main(String[] args) {
        flights.add(new Flight(
            "AI101", "Air India", "Mumbai", "Delhi", 50, 5000
        ));

        flights.add(new Flight(
            "6E202", "IndiGo", "Pune", "Bengaluru", 40, 4000
        ));

        flights.add(new Flight(
            "SG303", "SpiceJet", "Delhi", "Hyderabad", 30, 3500
        ));

        System.out.println("Welcome to Flight Booking System!");

        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. View Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. View Booking");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    viewFlights();
                    break;

                case "2":
                    bookFlight();
                    break;

                case "3":
                    viewBooking();
                    break;

                case "4":
                    System.out.println(
                        "Thank you for using Flight Booking System!"
                    );
                    sc.close();
                    return;

                default:
                    System.out.println(
                        "Invalid choice! Please select 1 to 4."
                    );
            }
        }
    }

    private static void viewFlights() {
        System.out.println("\n========== AVAILABLE FLIGHTS ==========");

        for (Flight flight : flights) {
            flight.display();
        }
    }

    private static void bookFlight() {
        viewFlights();

        System.out.print("\nEnter flight code: ");
        String code = sc.nextLine().trim();

        Flight selectedFlight = null;

        for (Flight flight : flights) {
            if (flight.getCode().equalsIgnoreCase(code)) {
                selectedFlight = flight;
                break;
            }
        }

        if (selectedFlight == null) {
            System.out.println("Invalid flight code!");
            return;
        }

        System.out.print("Enter passenger name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty() || !name.matches("[\\p{L} ]+")) {
            System.out.println("Invalid passenger name!");
            return;
        }

        int age;
        int tickets;

        try {
            System.out.print("Enter passenger age: ");
            age = Integer.parseInt(sc.nextLine().trim());

            if (age < 1 || age > 120) {
                System.out.println("Invalid age!");
                return;
            }

            System.out.print("Enter number of tickets: ");
            tickets = Integer.parseInt(sc.nextLine().trim());

            if (tickets <= 0) {
                System.out.println(
                    "Tickets must be greater than zero."
                );
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numeric values.");
            return;
        }

        if (tickets > selectedFlight.getAvailableSeats()) {
            System.out.println("Insufficient seats available!");
            return;
        }

        System.out.println("1. Regular Passenger");
        System.out.println("2. Student Passenger");
        System.out.print("Select passenger type: ");

        String type = sc.nextLine().trim();
        Passenger passenger;

        if (type.equals("1")) {
            passenger = new RegularPassenger(name, age);
        } else if (type.equals("2")) {
            passenger = new StudentPassenger(name, age);
        } else {
            System.out.println("Invalid passenger type!");
            return;
        }

        double baseTotal =
            selectedFlight.getBaseFare() * tickets;

        double discountAmount =
            baseTotal * passenger.getDiscount();

        double totalFare = baseTotal - discountAmount;

        System.out.printf("Base Fare: Rs. %.2f%n", baseTotal);
        System.out.printf("Discount: Rs. %.2f%n", discountAmount);
        System.out.printf("Final Fare: Rs. %.2f%n", totalFare);

        System.out.print("Confirm booking? (yes/no): ");
        String confirm = sc.nextLine().trim();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("Booking cancelled.");
            return;
        }

        if (!selectedFlight.bookSeats(tickets)) {
            System.out.println("Unable to book requested seats.");
            return;
        }

        Booking booking = new Booking(
            passenger, selectedFlight, tickets, totalFare
        );

        bookings.add(booking);

        System.out.println("\nBooking successful!");
        booking.display();
    }

    private static void viewBooking() {
        if (bookings.isEmpty()) {
            System.out.println("No confirmed booking found.");
        } else {
            bookings.get(bookings.size() - 1).display();
        }
    }
}

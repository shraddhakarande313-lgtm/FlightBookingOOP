from abc import ABC, abstractmethod
from datetime import datetime


class Flight:
    def __init__(self, code, airline, source, destination, seats, base_fare):
        self.code = code
        self.airline = airline
        self.source = source
        self.destination = destination
        self.seats = seats
        self.base_fare = base_fare

    def display(self):
        print(f"{self.code} | {self.airline} | {self.source} -> {self.destination} | Seats: {self.seats} | Fare: Rs.{self.base_fare}")


class Passenger(ABC):
    def __init__(self, name, age):
        self.name = name
        self.age = age

    @abstractmethod
    def get_discount(self):
        pass


class RegularPassenger(Passenger):
    def get_discount(self):
        return 0.0


class StudentPassenger(Passenger):
    def get_discount(self):
        return 0.10


class Booking:
    next_id = 1001

    def __init__(self, passenger, flight, tickets, total_fare):
        self.booking_id = Booking.next_id
        Booking.next_id += 1
        self.passenger = passenger
        self.flight = flight
        self.tickets = tickets
        self.total_fare = total_fare
        self.date = datetime.now().strftime("%d-%m-%Y %H:%M")

    def display(self):
        print("\n========== BOOKING SUMMARY ==========")
        print("Booking ID:", self.booking_id)
        print("Passenger:", self.passenger.name)
        print("Age:", self.passenger.age)
        print("Passenger Type:", type(self.passenger).__name__)
        print("Flight:", self.flight.code, self.flight.airline)
        print("Route:", self.flight.source, "to", self.flight.destination)
        print("Tickets:", self.tickets)
        print("Total Fare: Rs.", round(self.total_fare, 2))
        print("Booking Date:", self.date)
        print("=====================================")


class FlightBookingSystem:
    def __init__(self):
        self.flights = [
            Flight("AI101", "Air India", "Mumbai", "Delhi", 50, 5000),
            Flight("6E202", "IndiGo", "Pune", "Bengaluru", 40, 4000),
            Flight("SG303", "SpiceJet", "Delhi", "Hyderabad", 30, 3500)
        ]
        self.bookings = []

    def view_flights(self):
        print("\n========== AVAILABLE FLIGHTS ==========")
        for flight in self.flights:
            flight.display()

    def book_flight(self):
        self.view_flights()
        code = input("\nEnter flight code: ").strip().upper()

        flight = next((f for f in self.flights if f.code == code), None)

        if flight is None:
            print("Invalid flight code!")
            return

        name = input("Enter passenger name: ").strip()
        if not name or not name.replace(" ", "").isalpha():
            print("Invalid passenger name!")
            return

        try:
            age = int(input("Enter passenger age: "))
            tickets = int(input("Enter number of tickets: "))
        except ValueError:
            print("Invalid numeric input!")
            return

        if not 1 <= age <= 120 or tickets <= 0:
            print("Invalid age or ticket count!")
            return

        if tickets > flight.seats:
            print("Insufficient seats available!")
            return

        passenger_type = input("1. Regular Passenger  2. Student Passenger: ").strip()

        if passenger_type == "1":
            passenger = RegularPassenger(name, age)
        elif passenger_type == "2":
            passenger = StudentPassenger(name, age)
        else:
            print("Invalid passenger type!")
            return

        base_total = flight.base_fare * tickets
        discount_amount = base_total * passenger.get_discount()
        total_fare = base_total - discount_amount

        print("Base Fare: Rs.", base_total)
        print("Discount: Rs.", round(discount_amount, 2))
        print("Final Fare: Rs.", round(total_fare, 2))

        confirm = input("Confirm booking? (yes/no): ").strip().lower()
        if confirm != "yes":
            print("Booking cancelled.")
            return

        booking = Booking(passenger, flight, tickets, total_fare)
        flight.seats -= tickets
        self.bookings.append(booking)

        print("\nBooking successful!")
        booking.display()

    def view_booking(self):
        if not self.bookings:
            print("No confirmed booking found.")
        else:
            self.bookings[-1].display()

    def run(self):
        print("Welcome to Flight Booking System!")

        while True:
            print("\n1. View Flights")
            print("2. Book Flight")
            print("3. View Booking")
            print("4. Exit")

            choice = input("Enter your choice: ").strip()

            if choice == "1":
                self.view_flights()
            elif choice == "2":
                self.book_flight()
            elif choice == "3":
                self.view_booking()
            elif choice == "4":
                print("Thank you for using Flight Booking System!")
                break
            else:
                print("Invalid choice! Please select between 1 and 4.")


if __name__ == "__main__":
    system = FlightBookingSystem()
    system.run()

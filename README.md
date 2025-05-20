# Smart Home Simulation

This project is a Java-based simulation for a smart home management system, developed as part of the GUI course at the Polish-Japanese Academy of Information Technology (PJATK). The simulation allows users to create and manage virtual houses, rooms, and various smart devices in a user-friendly graphical interface.

## Features

- **House Management**: Add, edit, and remove virtual houses.
- **Room Management**: Organize rooms within each house and manage their properties.
- **Device Management**: Add, simulate, and control smart devices (e.g., lights, sensors, thermostats) within rooms.
- **Simulation**: Interact with and simulate the operation of devices, with feedback and status updates.
- **User-Friendly Interface**: Manage all entities (houses, rooms, devices) through an intuitive GUI.

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle (for building and running the project)

### Building and Running

1. **Clone the repository**
    ```sh
    git clone https://github.com/kenvez/smart-home.git
    cd smart-home
    ```

2. **Build the project using Gradle**
    ```sh
    ./gradlew build
    ```

3. **Run the simulation**
    ```sh
    ./gradlew run
    ```

   Alternatively, you can run the main class directly from your IDE (e.g., IntelliJ IDEA or Eclipse).

## Testing

This project uses **JUnit 5** for unit testing. To run tests:

```sh
./gradlew test
```

## Author

- **kenvez**  
  Developed as part of studies at PJATK (Polish-Japanese Academy of Information Technology)

## License

This project is for educational purposes as part of a university course.
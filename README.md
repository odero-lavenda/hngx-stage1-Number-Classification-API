# hngx-stage1-Number-Classification-API

# Number Classification API

This is a Number Classification API that classifies numbers as even or odd, checks if they are prime, and fetches fun facts about them using an external API.

## Setup Instructions

### Prerequisites:
- Java 11+
- Maven

### Installation:
1. Clone the repository:
    ```bash
    git clone https://github.com/username/repository.git
    ```
2. Navigate to the project directory:
    ```bash
    cd repository
    ```
3. Run the project:
    ```bash
    mvn spring-boot:run
    ```

### Usage:
- Send a GET request to `/api/classify-number?number=<number>`.

### Example:
```bash
GET http://localhost:8080/api/classify-number?number=371

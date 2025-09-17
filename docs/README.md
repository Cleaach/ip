# Udin - Personal Task Manager

![Udin Logo](docs/Ui.png)

Udin is a personal task management application that helps you organize and track your daily tasks. It supports three types of tasks: todos, deadlines, and events, with both command-line and graphical user interfaces.

## Features

- ✅ **Task Management**: Create, mark, unmark, and delete tasks
- 📅 **Deadlines**: Set due dates for tasks
- 🎯 **Events**: Schedule tasks with start and end times
- 🔍 **Search**: Find tasks by keywords
- 💾 **Data Persistence**: Tasks are automatically saved and loaded
- 🖥️ **Dual Interface**: Both CLI and GUI modes available

## Quick Start

### Prerequisites
- Java 17 or higher
- Gradle (included in the project)

### Running the Application

#### Option 1: Command Line Interface (CLI)
```bash
./gradlew run
```

#### Option 2: Graphical User Interface (GUI)
```bash
./gradlew run
```
The GUI will open automatically with a modern interface featuring:
- Chat-like interaction with Udin
- Task list display
- Easy command input

## Commands

### Basic Commands

| Command | Description | Example |
|---------|-------------|---------|
| `help` | Show all available commands | `help` |
| `list` | Display all tasks | `list` |
| `bye` | Exit the application | `bye` |

### Task Management

| Command | Description | Example |
|---------|-------------|---------|
| `todo <description>` | Add a todo task | `todo Buy groceries` |
| `deadline <description> /by <date>` | Add a deadline task | `deadline Submit report /by 2024-12-25 1800` |
| `event <description> /from <date> /to <date>` | Add an event task | `event Team meeting /from 2024-12-25 1400 /to 2024-12-25 1600` |
| `mark <number>` | Mark a task as done | `mark 1` |
| `unmark <number>` | Mark a task as not done | `unmark 1` |
| `delete <number>` | Delete a task | `delete 1` |
| `find <keyword>` | Search for tasks | `find meeting` |

### Date Format
- Use format: `yyyy-MM-dd HHmm`
- Example: `2024-12-25 1800` (December 25, 2024 at 6:00 PM)

## Usage Examples

### Adding Tasks
```
> todo Buy groceries
Got it. I've added this task:
  [T][ ] Buy groceries
Now you have 1 tasks in the list.

> deadline Submit project /by 2024-12-30 2359
Got it. I've added this task:
  [D][ ] Submit project (by: Dec 30 2024, 11:59PM)
Now you have 2 tasks in the list.

> event Team meeting /from 2024-12-25 1400 /to 2024-12-25 1600
Got it. I've added this task:
  [E][ ] Team meeting (from: Dec 25 2024, 2:00PM to: Dec 25 2024, 4:00PM)
Now you have 3 tasks in the list.
```

### Managing Tasks
```
> list
Your tasks:
 1.[T][ ] Buy groceries
 2.[D][ ] Submit project (by: Dec 30 2024, 11:59PM)
 3.[E][ ] Team meeting (from: Dec 25 2024, 2:00PM to: Dec 25 2024, 4:00PM)

> mark 1
Good boy! This task is all done:
[T][X] Buy groceries

> find meeting
Here are the matching tasks in your list:
 1.[E][ ] Team meeting (from: Dec 25 2024, 2:00PM to: Dec 25 2024, 4:00PM)
```

## Data Storage

- Tasks are automatically saved to `data/tasks.txt`
- Data persists between application sessions
- Tasks are saved immediately after any modification (add, mark, unmark, delete)

## Error Handling

The application provides helpful error messages for common issues:

- **Invalid task number**: `Invalid task number.`
- **Empty todo description**: `The description of a todo cannot be empty.`
- **Invalid date format**: `Please enter date as yyyy-MM-dd HHmm (e.g. 2019-12-02 1800).`
- **Missing search keyword**: `Please provide a keyword to find.`

## Development

### Running Tests
```bash
./gradlew test
```

### Building the Project
```bash
./gradlew build
```

### Project Structure
```
src/
├── main/java/udin/
│   ├── Udin.java          # Main application class
│   ├── Task.java          # Base task class
│   ├── ToDo.java          # Todo task implementation
│   ├── Deadline.java      # Deadline task implementation
│   ├── Event.java         # Event task implementation
│   ├── TaskList.java      # Task collection management
│   ├── Parser.java        # Command parsing and execution
│   ├── Storage.java       # Data persistence
│   ├── Ui.java            # CLI user interface
│   ├── MainWindow.java    # GUI controller
│   └── DialogBox.java     # GUI dialog components
└── test/java/udin/        # Comprehensive test suite
```

## License

This project is part of the CS2103T Software Engineering course.

---

**Happy task managing with Udin! 🎯**

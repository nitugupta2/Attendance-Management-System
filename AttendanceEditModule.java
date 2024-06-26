import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Attendance class representing the attendance record
class Attendance {
    private int id;
    private int studentId;
    private String date;
    private String status;
    private String remarks;

    public Attendance(int id, int studentId, String date, String status, String remarks) {
        this.id = id;
        this.studentId = studentId;
        this.date = date;
        this.status = status;
        this.remarks = remarks;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getRemarks() { return remarks; }

    public void setStudentId(int studentId) { this.studentId = studentId; }
    public void setDate(String date) { this.date = date; }
    public void setStatus(String status) { this.status = status; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}

// AttendanceManager class to manage attendance records
class AttendanceManager {
    private List<Attendance> attendanceList = new ArrayList<>();
    private int nextId = 1;

    public List<Attendance> getAllAttendances() {
        return attendanceList;
    }

    public Attendance getAttendanceById(int id) {
        return attendanceList.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    public void addAttendance(Attendance attendance) {
        attendanceList.add(attendance);
    }

    public void updateAttendance(Attendance attendance) {
        Attendance existing = getAttendanceById(attendance.getId());
        if (existing != null) {
            existing.setStudentId(attendance.getStudentId());
            existing.setDate(attendance.getDate());
            existing.setStatus(attendance.getStatus());
            existing.setRemarks(attendance.getRemarks());
        }
    }

    public void deleteAttendance(int id) {
        attendanceList.removeIf(a -> a.getId() == id);
    }

    public int getNextId() {
        return nextId++;
    }
}

// AttendanceEditModule class to display and edit attendance records using Swing
public class AttendanceEditModule {
    private AttendanceManager attendanceManager;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    public AttendanceEditModule(AttendanceManager attendanceManager) {
        this.attendanceManager = attendanceManager;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Attendance Edit Module");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        tableModel = new DefaultTableModel(new String[]{"ID", "Student ID", "Date", "Status", "Remarks"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 760, 300);
        frame.add(scrollPane);

        JButton editButton = new JButton("Edit");
        editButton.setBounds(10, 320, 100, 30);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    Attendance attendance = attendanceManager.getAttendanceById(id);
                    if (attendance != null) {
                        editAttendance(attendance);
                    }
                }
            }
        });
        frame.add(editButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(120, 320, 100, 30);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    attendanceManager.deleteAttendance(id);
                    tableModel.removeRow(selectedRow);
                }
            }
        });
        frame.add(deleteButton);

        // Panel to add new attendance records
        JPanel addPanel = new JPanel();
        addPanel.setBackground(new Color(200, 200, 200));
        addPanel.setBounds(10, 360, 760, 200);
        addPanel.setLayout(new GridLayout(6, 2, 10, 10));
        frame.add(addPanel);

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setForeground(Color.BLUE);
        JTextField studentIdField = new JTextField();
        studentIdField.setForeground(Color.BLUE);
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setForeground(Color.BLUE);
        JTextField dateField = new JTextField();
        dateField.setForeground(Color.BLUE);
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setForeground(Color.BLUE);
        JTextField statusField = new JTextField();
        statusField.setForeground(Color.BLUE);
        JLabel remarksLabel = new JLabel("Remarks:");
        remarksLabel.setForeground(Color.BLUE);
        JTextField remarksField = new JTextField();
        remarksField.setForeground(Color.BLUE);

        JButton addButton = new JButton("Submit");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int studentId = Integer.parseInt(studentIdField.getText());
                String date = dateField.getText();
                String status = statusField.getText();
                String remarks = remarksField.getText();
                Attendance newAttendance = new Attendance(attendanceManager.getNextId(), studentId, date, status, remarks);
                attendanceManager.addAttendance(newAttendance);
                loadAttendanceData();
                studentIdField.setText("");
                dateField.setText("");
                statusField.setText("");
                remarksField.setText("");
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentIdField.setText("");
                dateField.setText("");
                statusField.setText("");
                remarksField.setText("");
            }
        });

        addPanel.add(studentIdLabel);
        addPanel.add(studentIdField);
        addPanel.add(dateLabel);
        addPanel.add(dateField);
        addPanel.add(statusLabel);
        addPanel.add(statusField);
        addPanel.add(remarksLabel);
        addPanel.add(remarksField);
        addPanel.add(addButton);
        addPanel.add(clearButton);

        loadAttendanceData();
    }

    private void loadAttendanceData() {
        tableModel.setRowCount(0);
        for (Attendance attendance : attendanceManager.getAllAttendances()) {
            tableModel.addRow(new Object[]{attendance.getId(), attendance.getStudentId(), attendance.getDate(), attendance.getStatus(), attendance.getRemarks()});
        }
    }

    private void editAttendance(Attendance attendance) {
        JTextField studentIdField = new JTextField(String.valueOf(attendance.getStudentId()));
        JTextField dateField = new JTextField(attendance.getDate());
        JTextField statusField = new JTextField(attendance.getStatus());
        JTextField remarksField = new JTextField(attendance.getRemarks());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);
        panel.add(new JLabel("Remarks:"));
        panel.add(remarksField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Attendance", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            attendance.setStudentId(Integer.parseInt(studentIdField.getText()));
            attendance.setDate(dateField.getText());
            attendance.setStatus(statusField.getText());
            attendance.setRemarks(remarksField.getText());
            attendanceManager.updateAttendance(attendance);
            loadAttendanceData();
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        AttendanceManager manager = new AttendanceManager();
        manager.addAttendance(new Attendance(manager.getNextId(), 101, "2024-06-25", "Present", "On time"));
        manager.addAttendance(new Attendance(manager.getNextId(), 102, "2024-06-25", "Absent", "Sick"));

        AttendanceEditModule module = new AttendanceEditModule(manager);
        module.show();
    }
}
public class CustomerDTO {
    private int id;
    private String name;
    private String email;
    private long phone;
    private String address;

    // Constructor
    public CustomerDTO(int id, String name, String email, long phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public long getPhone() { return phone; }
    public void setPhone(long phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

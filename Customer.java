public class Customer {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;

    public Customer(String name,String address,String phoneNumber,String email,String dateOfBirth){
        this.name=name;
        this.address=address;
        this.phoneNumber=phoneNumber;
        this.email=email;
        this.dateOfBirth=dateOfBirth;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public String getEmail(){
        return email;
    }
    public String getdateOfBirth(){
        return dateOfBirth;
        // deded
    }

}

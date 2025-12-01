package tarc.edu.my.model;

import java.time.LocalDate;

/**
 *
 * @author JiaQian
 */
public abstract class Person
{

    /* Data Fields */
    private String name;
    private LocalDate DOB;
    private String contactNo;
    private String email;
    private LocalDate dateJoined;

    /* Person - No-arg Constructors */
    public Person()
    {
        this("", null, "", "");

    }

    /* Person - Parameterized Constructor*/
    public Person(String name, LocalDate DOB, String contactNo, String email)
    {
        this.name = name;
        this.DOB = DOB;
        this.contactNo = contactNo;
        this.email = email;
        dateJoined = LocalDate.now();
    }

    /* Getters */
    // Name
    public String getName()
    {
        return name;
    }

    // DOB
    public String getDOB()
    {
        return DOB.toString();
    }

    // ContactNo
    public String getContactNo()
    {
        return contactNo;
    }

    // Email
    public String getEmail()
    {
        return email;
    }

    // DateJoined
    public String getDateJoined()
    {
        return dateJoined.toString();
    }

    /* Setters */
    // Name
    public void setName(String name)
    {
        this.name = name;
    }

    // DOB
    public void setDOB(int year, int month, int dayOfMonth)
    {
        DOB = LocalDate.of(year, month, dayOfMonth);
    }

    public void setDOB(LocalDate DOB)
    {
        this.DOB = DOB;
    }

    // ContactNo
    public void setContactNo(String contactNo)
    {
        this.contactNo = contactNo;
    }

    // Email
    public void setEmail(String email)
    {
        this.email = email;
    }

    // DateJoined
    public void setDateJoined(int year, int month, int dayOfMonth)
    {
        dateJoined = LocalDate.of(year, month, dayOfMonth);
    }

    public void setDateJoined(LocalDate dateJoined)
    {
        this.dateJoined = dateJoined;
    }

}

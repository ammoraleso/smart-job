package co.com.andres.domain;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "phones")
@Data
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    @Column(name="citycode")
    private String cityCode;
    @Column(name="contrycode")
    private String countryCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

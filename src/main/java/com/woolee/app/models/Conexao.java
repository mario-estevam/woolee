package com.woolee.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Conexao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "remetente_id", updatable = false)
    private User remetente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destinatario_id", updatable = false)
    private User destinatario;

    @Column
    private Date dataConexao;

    @Column
    private Boolean situacao;

    @Column
    private Date deletedAt;

}

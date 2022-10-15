package com.woolee.app.dtos;

import com.woolee.app.models.Postagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class PostagemDTO {
    private Postagem postagem;
    private Boolean curtido;
}

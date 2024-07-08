package com.golym.mylog.model.dto.response;

import com.golym.mylog.model.dto.common.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseCreateCategoryDto {

    private ResponseDto response;
    private CategoryDto category;
}

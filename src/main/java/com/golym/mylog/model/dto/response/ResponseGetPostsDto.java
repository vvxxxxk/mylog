package com.golym.mylog.model.dto.response;

import com.golym.mylog.model.dto.common.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseGetPostsDto {

    private ResponseDto response;
    private List<PostDto> postList;
}

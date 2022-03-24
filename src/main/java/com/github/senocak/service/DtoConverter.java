package com.github.senocak.service;

import com.github.senocak.dto.auth.RoleResponse;
import com.github.senocak.dto.auth.UserResponse;
import com.github.senocak.entity.Role;
import com.github.senocak.entity.User;

import java.util.Date;
import java.util.stream.Collectors;

public class DtoConverter {
    private DtoConverter(){}
/*
    /**
     * @param post -- Post object to convert to dto object
     * @return -- PostDto object
     * /
    public static PostDto convertPostEntityToPostsDto(Post post){
        PostDto postsDto = new PostDto();
        postsDto.setResourceId(post.getId());
        postsDto.setTitle(post.getTitle());
        postsDto.setSlug(post.getSlug());
        postsDto.setBody(post.getBody());
        if (Objects.nonNull(post.getCategories()) && !post.getCategories().isEmpty()){
            postsDto.setCategories(
                    post.getCategories()
                            .stream()
                            .map(c -> {
                                c.setPosts(null);
                                return DtoConverter.convertEntityToDto(c);
                            })
                            .collect(Collectors.toList())
            );
        }
        if (Objects.nonNull(post.getComments()) && !post.getComments().isEmpty()){
            postsDto.setComments(
                    post.getComments()
                            .stream()
                            .filter(Comment::isApproved)
                            .map(DtoConverter::convertEntityToDto)
                            .collect(Collectors.toList())
            );
        }
        List<String> tags = new ArrayList<>();
        if (Objects.nonNull(post.getTags()) && !post.getTags().isEmpty())
            tags = post.getTags();
        postsDto.setTags(tags);
        postsDto.setUser(convertEntityToDto(post.getUser()));
        postsDto.setCreatedAt(convertDateToLong(post.getCreatedAt()));
        postsDto.setUpdatedAt(convertDateToLong(post.getUpdatedAt()));
        return postsDto;
    }

    /**
     * @param category -- Category object to convert to dto object
     * @return -- CategoryDto object
     * /
    public static CategoryDto convertEntityToDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setResourceId(category.getId());
        categoryDto.setSlug(category.getSlug());
        categoryDto.setName(category.getName());
        categoryDto.setImage(CategoryController.URL + "/" + category.getSlug() + "/image");
        categoryDto.setResourceUrl(CategoryController.URL + "/" + category.getSlug());
        if (Objects.nonNull(category.getPosts()) && !category.getPosts().isEmpty()){
            categoryDto.setPostDto(
                    category.getPosts().stream()
                            .map(p -> {
                                // Since there are 2 way relationship, we need to set to the one way relation to null to avoid StackOverflowError
                                p.setCategories(null);
                                return DtoConverter.convertPostEntityToPostsDto(p);
                            })
                            .collect(Collectors.toList())
            );
        }
        return categoryDto;
    }

    /**
     * @param comment -- Comment object to convert to dto object
     * @return -- CommentDto object
     * /
    public static CommentDto convertEntityToDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setResourceId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        commentDto.setCreatedAt(convertDateToLong(comment.getCreatedAt()));
        commentDto.setApproved(comment.isApproved());
        return commentDto;
    }
*/
    /**
     * @param user -- User object to convert to dto object
     * @return -- UserResponse object
     */
    public static UserResponse convertEntityToDto(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setUsername(user.getUsername());
        userResponse.setRoles(
                user.getRoles().stream().map(DtoConverter::convertEntityToDto).collect(Collectors.toSet())
        );
        userResponse.setResourceUrl("UserController.URL" + "/" + user.getUsername());
        return userResponse;
    }

    /**
     * @param role -- role object to convert to dto object
     * @return -- RoleResponse object
     */
    public static RoleResponse convertEntityToDto(Role role){
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName(role.getName());
        return roleResponse;
    }

    /**
     * @param date -- Date object to convert to long timestamp
     * @return -- converted timestamp object that is long type
     */
    public static long convertDateToLong(Date date){
        return date.getTime() / 1000;
    }
}

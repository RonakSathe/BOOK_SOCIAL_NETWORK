package io.rons.book.feedback;

import io.rons.book.book.Book;
import io.rons.book.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@SuperBuilder
public class Feedback extends BaseEntity {

    private Double note; // 1-5 stars
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

}

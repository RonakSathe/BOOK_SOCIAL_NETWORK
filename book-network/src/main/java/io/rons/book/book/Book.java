package io.rons.book.book;


import io.rons.book.common.BaseEntity;
import io.rons.book.feedback.Feedback;
import io.rons.book.history.BookTransactionHistory;
import io.rons.book.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter@Setter@SuperBuilder@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;


    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate(){
        if(feedbacks == null || feedbacks.isEmpty()){
            return  0.0;
        }

        var rate = this.feedbacks.stream().mapToDouble(Feedback::getNote)
                .average().orElse(0.0);
        // 3.25 --> 3.0 || 3.65-->4.0

        return Math.round(rate*10.0)/10.0;
    }

}

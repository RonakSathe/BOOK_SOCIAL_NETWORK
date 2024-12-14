package io.rons.book.book;

import lombok.*;

@AllArgsConstructor@NoArgsConstructor
@Getter@Setter@Builder
public class BorrowedBookResponse {
    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private double rate;
    private boolean returned;
    private boolean returnApproved;

}

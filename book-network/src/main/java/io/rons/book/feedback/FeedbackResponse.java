package io.rons.book.feedback;

import lombok.*;

@Getter@Setter
@Builder@AllArgsConstructor@NoArgsConstructor
public class FeedbackResponse {

    private Double note;
    private String comment;
    private boolean ownFeedback;


}

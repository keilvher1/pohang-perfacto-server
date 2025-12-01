package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

/**
 * 좋아요 엔티티
 * 사용자가 장소에 대해 좋아요를 누른 정보
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "likes",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "place_id"})
    },
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_place_id", columnList = "place_id")
    }
)
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 좋아요를 누른 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place; // 좋아요가 눌린 장소

    /**
     * 정적 팩토리 메서드
     */
    public static Like create(User user, Place place) {
        Like like = Like.builder()
            .user(user)
            .place(place)
            .build();

        place.incrementLikeCount();
        return like;
    }

    /**
     * 좋아요 삭제 전 처리
     */
    @PreRemove
    public void preRemove() {
        if (this.place != null) {
            this.place.decrementLikeCount();
        }
    }
}

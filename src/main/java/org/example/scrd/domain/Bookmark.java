package org.example.scrd.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.scrd.BaseEntity;

/**
 * 북마크 엔티티
 * 사용자가 장소를 북마크한 정보
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "bookmarks",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "place_id"})
    },
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_place_id", columnList = "place_id")
    }
)
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 북마크한 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place; // 북마크된 장소

    @Column(length = 500)
    private String memo; // 개인 메모

    /**
     * 정적 팩토리 메서드
     */
    public static Bookmark create(User user, Place place, String memo) {
        Bookmark bookmark = Bookmark.builder()
            .user(user)
            .place(place)
            .memo(memo)
            .build();

        place.incrementBookmarkCount();
        return bookmark;
    }

    /**
     * 북마크 삭제 전 처리
     */
    @PreRemove
    public void preRemove() {
        if (this.place != null) {
            this.place.decrementBookmarkCount();
        }
    }
}

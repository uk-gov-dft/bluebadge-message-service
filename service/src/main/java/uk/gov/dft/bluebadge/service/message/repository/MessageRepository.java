package uk.gov.dft.bluebadge.service.message.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.message.repository.domain.PasswordResetEntity;

@Component
public class MessageRepository {

    private SqlSession session;

    public MessageRepository(SqlSession session) {
        this.session = session;
    }

    public int createPasswordReset(PasswordResetEntity passwordResetEntity){
        return session.insert("createPasswordReset", passwordResetEntity);
    }
}

package uk.gov.dft.bluebadge.service.message.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.dft.bluebadge.service.message.service.TemplateName.APPLICATION_SUBMITTED;
import static uk.gov.dft.bluebadge.service.message.service.TemplateName.PASSWORD_RESET_SUCCESS;

import com.google.common.collect.ImmutableMap;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.model.message.generated.MessageDetails;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyClient;
import uk.gov.dft.bluebadge.service.message.client.notify.NotifyTemplates;
import uk.gov.dft.bluebadge.service.message.client.referencedataservice.model.LocalAuthorityRefData;
import uk.gov.dft.bluebadge.service.message.repository.domain.MessageEntity;
import uk.gov.dft.bluebadge.service.message.service.referencedata.ReferenceDataService;

public class MessageServiceTest {

  private static final TemplateName TEST_TEMPLATE = TemplateName.NEW_USER;
  private MessageService service;

  @Mock private NotifyClient mockNotifyClient;
  @Mock private NotifyTemplates mockNotifyTemplates;
  @Mock private NotifySecretsManager mockSecretsManager;
  @Mock private ReferenceDataService mockReferenceDataService;
  private NotifyProfile testNotifyProfile;

  private NotifyProfile oldProfile;
  private NotifyProfile newProfile;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    service =
        new MessageService(
            mockNotifyClient, mockNotifyTemplates, mockSecretsManager, mockReferenceDataService);

    testNotifyProfile =
        NotifyProfile.builder()
            .apiKey("la notify api key")
            .templates(ImmutableMap.of(TEST_TEMPLATE, "la notify template id"))
            .build();
  }

  @Test
  @SneakyThrows
  public void sendMessage_dftMessage() {
    UUID notifyRef = UUID.randomUUID();
    when(mockNotifyClient.dftEmailMessage(any(), any(), any())).thenReturn(notifyRef);
    when(mockNotifyTemplates.getNotifyTemplate(TEST_TEMPLATE)).thenReturn("test template");

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TEST_TEMPLATE);
    messageDetails.setEmailAddress("bob@bob.com");
    //    messageDetails.setMessageAttributes();

    MessageEntity result = service.sendMessage(messageDetails);

    assertThat(result).isNotNull();
    assertThat(result.getBbbReference()).isNotNull();
    assertThat(result.getBbbReference()).isNotEqualTo(notifyRef);
    assertThat(result.getNotifyReference()).isEqualTo(notifyRef);
    assertThat(result.getTemplate()).isEqualTo(TEST_TEMPLATE.name());

    verify(mockNotifyClient)
        .dftEmailMessage("test template", messageDetails, result.getBbbReference());
  }

  @Test
  @SneakyThrows
  public void sendMessage_laMessage() {
    UUID notifyRef = UUID.randomUUID();
    when(mockNotifyClient.laEmailMessage(any(), any(), any(), any())).thenReturn(notifyRef);
    when(mockSecretsManager.retrieveLANotifyProfile("SOME_LA")).thenReturn(testNotifyProfile);

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TEST_TEMPLATE);
    messageDetails.setEmailAddress("bob@bob.com");
    messageDetails.setLaShortCode("SOME_LA");

    MessageEntity result = service.sendMessage(messageDetails);

    assertThat(result).isNotNull();
    assertThat(result.getBbbReference()).isNotNull();
    assertThat(result.getBbbReference()).isNotEqualTo(notifyRef);
    assertThat(result.getNotifyReference()).isEqualTo(notifyRef);
    assertThat(result.getTemplate()).isEqualTo(TEST_TEMPLATE.name());

    verifyZeroInteractions(mockNotifyTemplates);
    verify(mockNotifyClient, never()).dftEmailMessage(any(), any(), any());
    verify(mockNotifyClient)
        .laEmailMessage(
            eq("la notify api key"), eq("la notify template id"), eq(messageDetails), any());
  }

  @Test
  @SneakyThrows
  public void sendMessage_whenErrorInLAMessage_thenDftMessageSent() {
    when(mockNotifyClient.laEmailMessage(any(), any(), any(), any()))
        .thenThrow(new RuntimeException("test error"));
    UUID notifyRef = UUID.randomUUID();
    when(mockNotifyClient.dftEmailMessage(any(), any(), any())).thenReturn(notifyRef);
    when(mockNotifyTemplates.getNotifyTemplate(TEST_TEMPLATE)).thenReturn("test template");
    when(mockSecretsManager.retrieveLANotifyProfile("SOME_LA")).thenReturn(testNotifyProfile);

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TEST_TEMPLATE);
    messageDetails.setEmailAddress("bob@bob.com");
    messageDetails.setLaShortCode("SOME_LA");

    MessageEntity result = service.sendMessage(messageDetails);

    assertThat(result).isNotNull();
    assertThat(result.getBbbReference()).isNotNull();
    assertThat(result.getBbbReference()).isNotEqualTo(notifyRef);
    assertThat(result.getNotifyReference()).isEqualTo(notifyRef);
    assertThat(result.getTemplate()).isEqualTo(TEST_TEMPLATE.name());

    verify(mockNotifyClient)
        .laEmailMessage(
            eq("la notify api key"), eq("la notify template id"), eq(messageDetails), any());
    verify(mockNotifyClient).dftEmailMessage(eq("test template"), eq(messageDetails), any());
  }

  @Test
  @SneakyThrows
  public void sendMessage_whenNoLaProfile_thenDftMessageSent() {
    when(mockNotifyClient.laEmailMessage(any(), any(), any(), any()))
        .thenThrow(new RuntimeException("test error"));
    UUID notifyRef = UUID.randomUUID();
    when(mockNotifyClient.dftEmailMessage(any(), any(), any())).thenReturn(notifyRef);
    when(mockNotifyTemplates.getNotifyTemplate(TEST_TEMPLATE)).thenReturn("test template");
    when(mockSecretsManager.retrieveLANotifyProfile("SOME_LA")).thenReturn(null);

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(TEST_TEMPLATE);
    messageDetails.setEmailAddress("bob@bob.com");
    messageDetails.setLaShortCode("SOME_LA");

    MessageEntity result = service.sendMessage(messageDetails);

    assertThat(result).isNotNull();
    assertThat(result.getBbbReference()).isNotNull();
    assertThat(result.getBbbReference()).isNotEqualTo(notifyRef);
    assertThat(result.getNotifyReference()).isEqualTo(notifyRef);
    assertThat(result.getTemplate()).isEqualTo(TEST_TEMPLATE.name());

    verify(mockNotifyClient, never()).laEmailMessage(any(), any(), any(), any());
    verify(mockNotifyClient).dftEmailMessage(eq("test template"), eq(messageDetails), any());
  }

  @Test
  @SneakyThrows
  public void sendMessage_whenNoLaTemplate_thenDftMessageSent() {
    when(mockNotifyClient.laEmailMessage(any(), any(), any(), any()))
        .thenThrow(new RuntimeException("test error"));
    UUID notifyRef = UUID.randomUUID();
    when(mockNotifyClient.dftEmailMessage(any(), any(), any())).thenReturn(notifyRef);
    when(mockNotifyTemplates.getNotifyTemplate(APPLICATION_SUBMITTED)).thenReturn("test template");
    when(mockSecretsManager.retrieveLANotifyProfile("SOME_LA")).thenReturn(testNotifyProfile);

    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(APPLICATION_SUBMITTED);
    messageDetails.setEmailAddress("bob@bob.com");
    messageDetails.setLaShortCode("SOME_LA");

    MessageEntity result = service.sendMessage(messageDetails);

    assertThat(result).isNotNull();
    assertThat(result.getBbbReference()).isNotNull();
    assertThat(result.getBbbReference()).isNotEqualTo(notifyRef);
    assertThat(result.getNotifyReference()).isEqualTo(notifyRef);
    assertThat(result.getTemplate()).isEqualTo(APPLICATION_SUBMITTED.name());

    verify(mockNotifyClient, never()).laEmailMessage(any(), any(), any(), any());
    verify(mockNotifyClient).dftEmailMessage(eq("test template"), eq(messageDetails), any());
  }

  // Should never happen due to validation on start up.
  @Test(expected = RuntimeException.class)
  @SneakyThrows
  public void whenUnknownTemplate_thenException() {
    MessageDetails messageDetails = new MessageDetails();
    messageDetails.setTemplate(APPLICATION_SUBMITTED);
    when(mockNotifyTemplates.getNotifyTemplate(APPLICATION_SUBMITTED)).thenReturn(null);
    service.sendMessage(messageDetails);
    verifyZeroInteractions(mockNotifyClient);
  }

  @Test
  public void mergeNofifyProfile_NoNew() {
    oldProfile = NotifyProfile.builder().apiKey("old").build();
    newProfile = null;

    assertThat(service.mergeNotifyProfiles(oldProfile, newProfile)).isEqualTo(oldProfile);
  }

  @Test
  public void mergeNofifyProfile_NoOld() {
    oldProfile = null;
    newProfile = NotifyProfile.builder().apiKey("new").build();

    assertThat(service.mergeNotifyProfiles(oldProfile, newProfile)).isEqualTo(newProfile);
  }

  @Test
  public void mergeNofifyProfile_mergeTemplates() {
    oldProfile =
        NotifyProfile.builder().templates(ImmutableMap.of(APPLICATION_SUBMITTED, "app")).build();
    newProfile =
        NotifyProfile.builder().templates(ImmutableMap.of(PASSWORD_RESET_SUCCESS, "reset")).build();
    NotifyProfile expected =
        NotifyProfile.builder()
            .templates(
                ImmutableMap.of(APPLICATION_SUBMITTED, "app", PASSWORD_RESET_SUCCESS, "reset"))
            .build();
    assertThat(service.mergeNotifyProfiles(oldProfile, newProfile)).isEqualTo(expected);
  }

  @Test(expected = BadRequestException.class)
  public void createOrUpdateNotifyProfile_invalidLa() {
    when(mockReferenceDataService.getLocalAuthority("SHROP")).thenReturn(null);
    service.createOrUpdateNotifyProfile("SHROP", NotifyProfile.builder().build());
  }

  @Test
  public void createOrUpdateNotifyProfile_create() {
    when(mockReferenceDataService.getLocalAuthority("SHROP"))
        .thenReturn(new LocalAuthorityRefData());
    when(mockSecretsManager.retrieveLANotifyProfile("SHROP"))
        .thenThrow(new NotFoundException("", NotFoundException.Operation.RETRIEVE));

    newProfile = NotifyProfile.builder().apiKey("dfg").build();
    service.createOrUpdateNotifyProfile("SHROP", newProfile);

    verify(mockSecretsManager, times(1)).createOrUpdateNotifyProfile("SHROP", newProfile);
  }

  @Test
  public void createOrUpdateNotifyProfile_update() {
    oldProfile = NotifyProfile.builder().apiKey("123").build();
    newProfile =
        NotifyProfile.builder().templates(ImmutableMap.of(APPLICATION_SUBMITTED, "app")).build();
    NotifyProfile expected = service.mergeNotifyProfiles(oldProfile, newProfile);

    when(mockReferenceDataService.getLocalAuthority("SHROP"))
        .thenReturn(new LocalAuthorityRefData());
    when(mockSecretsManager.retrieveLANotifyProfile("SHROP")).thenReturn(oldProfile);

    service.createOrUpdateNotifyProfile("SHROP", newProfile);
    verify(mockSecretsManager, times(1)).createOrUpdateNotifyProfile("SHROP", expected);
  }
}

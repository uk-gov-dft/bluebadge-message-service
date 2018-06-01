package uk.gov.dft.bluebadge.service.message.converter;

/**
 * Converts to and from API model and DB Entity Model.
 *
 * @param <ENTITYT> DB Entity bean
 * @param <MODELT> API Model bean
 */
interface BiConverter<ENTITYT, MODELT> {
  ENTITYT convertToEntity(MODELT model);

  MODELT convertToModel(ENTITYT entity);
}

package org.jboss.license.dictionary.license;

import org.jboss.license.dictionary.BadRequestException;
import org.jboss.license.dictionary.NotFoundException;
import org.jboss.license.dictionary.api.FullLicenseData;
import org.jboss.license.dictionary.api.License;
import org.jboss.license.dictionary.api.LicenseResource;
import org.jboss.license.dictionary.utils.ErrorDto;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jboss.license.dictionary.utils.Mappers.*;
import static org.jboss.license.dictionary.utils.ResponseUtils.valueOrNotFound;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * <br>
 * Date: 8/30/17
 */
@Path(LicenseResource.LICENSES)
public class LicenseResourceImpl implements LicenseResource {

    private static final Logger log = Logger.getLogger(LicenseResourceImpl.class);

    @Inject
    private LicenseStore licenseStore;

    @Override
    public List<License> getLicenses(
            String name,
            String url,
            String nameAlias,
            String urlAlias,
            String searchTerm) {
        long singleResultIndicatorCount = nonNullCount(name, url, nameAlias, urlAlias);
        if (singleResultIndicatorCount > 1) {
            throw new BadRequestException("Not more than one query parameter " +
                    "{name, url, nameAlias, urlAlias} should be provided");
        }

        if (singleResultIndicatorCount > 0) {
            if (searchTerm != null) {
                throw new BadRequestException("searchTerm cannot be mixed " +
                        "with neither of {name, url, nameAlias, urlAlias} query parameters");
            }

            LicenseEntity entity;
            if (name != null) {
                entity = valueOrNotFound(licenseStore.getForName(name), "No license was found for name %s", name);
            } else if (url != null) {
                entity = valueOrNotFound(licenseStore.getForUrl(url), "No license was found for url %s", url);
            } else if (nameAlias != null) {
                entity = valueOrNotFound(licenseStore.getForNameAlias(nameAlias), "Could not find license for nameAlias %s", nameAlias);
            } else {
                entity = valueOrNotFound(licenseStore.getForUrlAlias(urlAlias), "Could not find license for urlAlias %s", urlAlias);
            }
            return Collections.singletonList(limitedMapper.map(entity, License.class));
        } else {
            List<LicenseEntity> results;
            if (searchTerm != null) {
                results = licenseStore.findBySearchTerm(searchTerm)
                        .stream().collect(Collectors.toList());
            } else {
                results = licenseStore.getAll();
            }
            return limitedMapper.map(results, licenseListType);
        }
    }

    @Override
    @Transactional
    public License updateLicense(Integer licenseId, FullLicenseData license) {
        Optional<LicenseEntity> maybeLicenseEntity = licenseStore.getById(licenseId);

        LicenseEntity entity = maybeLicenseEntity.orElseThrow(
                () -> new NotFoundException("No license found for id " + licenseId));
        fullMapper.map(license, entity);

        return limitedMapper.map(entity, License.class);
    }

    @Override
    public void deleteLicense(Integer licenseId) {
        if (!licenseStore.delete(licenseId)) {
            throw new NotFoundException("No license found for id " + licenseId);
        }
    }

    @Override
    public License getLicense(Integer licenseId) {
        LicenseEntity entity =
                licenseStore.getById(licenseId)
                        .orElseThrow(() -> new NotFoundException("No license found for id " + licenseId));
        return fullMapper.map(entity, License.class);
    }

    @Override
    @Transactional
    public License addLicense(FullLicenseData license) {
        validate(license);
        license.getTextUrl();// mstodo fetch content and set to entity
        LicenseEntity entity = fullMapper.map(license, LicenseEntity.class);
        licenseStore.save(entity);
        return fullMapper.map(entity, License.class);
    }

    // mstodo: this does not work!
    private void validate(FullLicenseData license) {
        ErrorDto errors = new ErrorDto();
        licenseStore.getForName(license.getName())
                .ifPresent(
                        l -> errors.addError("License with the same name found. Conflicting license id: %d", l.getId())
                );
        licenseStore.getForUrl(license.getUrl())
                .ifPresent(
                        l -> errors.addError("License with the same url found. Conflicting license id: %d", l.getId())
                );

        license.getNameAliases().forEach(
                alias -> licenseStore
                        .getForNameAlias(alias)
                        .ifPresent(
                                l -> errors.addError("License with the same name alias found. Conflicting license id: %d", l.getId())
                        )

        );
        license.getUrlAliases().forEach(
                alias -> licenseStore.getForUrlAlias(alias)
                        .ifPresent(
                                l -> errors.addError("License with the same url alias found. Conflicting license id: %d", l.getId())
                        )

        );
    }

    private long nonNullCount(Object... args) {
        return Stream.of(args).filter(Objects::nonNull).count();
    }


}

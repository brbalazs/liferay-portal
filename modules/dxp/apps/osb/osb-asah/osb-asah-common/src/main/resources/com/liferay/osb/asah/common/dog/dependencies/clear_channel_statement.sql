BEGIN TRANSACTION;

DELETE FROM BlogDaily WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQEvent WHERE channelId IN ( ${channel_ids} ) AND eventDate < TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 90 MINUTE);
DELETE FROM BQEventProperty WHERE channelId IN ( ${channel_ids} ) AND eventDate < TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 90 MINUTE);
DELETE FROM BQIdentityActivitySummary WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQIdentityInterestPage WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQIdentityInterestScore WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQMembership WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQMembershipChange WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQMembershipIndividual WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQOrder WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQOrder_Raw WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQProduct WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQProduct_Raw WHERE channelId IN ( ${channel_ids} );
DELETE FROM BQSession WHERE channelId IN ( ${channel_ids} ) AND sessionStart < TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 90 MINUTE);
DELETE FROM BQSessionInterestScore WHERE channelId IN ( ${channel_ids} );
DELETE FROM CustomAssetDaily WHERE channelId IN ( ${channel_ids} );
DELETE FROM DocumentLibraryDaily WHERE channelId IN ( ${channel_ids} );
DELETE FROM FormDaily WHERE channelId IN ( ${channel_ids} );
DELETE FROM JournalDaily WHERE channelId IN ( ${channel_ids} );
DELETE FROM PageDaily WHERE channelId IN ( ${channel_ids} );

COMMIT TRANSACTION;
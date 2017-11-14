from azure.storage.blob import BlockBlobService
from io import BytesIO

__author__ = 'Miles Dowe'

"""
Azure Blob analog of get_s3_object.

To use:
    1. run `pip install azure-storage`

Vocab notes:
    bucket = account
    key    = container + blob
"""


def get_wav_file(account, item):
    # define blob service
    secret_key = 'tH8ymbZYcxL4sGu1SagQRRhk4ky/h8pzOE/WtZLNrFZNKjii64jJBX5/sKV9rZ5IYIJkMnGweFMhf1o+nEU0Pg=='

    block_blob_service = BlockBlobService(
        account_name=account,
        account_key=secret_key
    )

    # get wav file
        # note: code currently sends 'audio'; should I split, or hardcode?
    blob = block_blob_service.get_blob_to_bytes('audio', item[6:])

    return BytesIO(blob.content)

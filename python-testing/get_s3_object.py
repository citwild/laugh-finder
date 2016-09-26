import boto3
from io import BytesIO

__author__ = 'Miles Dowe'

"""
Uses Boto3 library to retrieve S3 object from AWS, then returns the
byte array body as a string (because later scripts originally read in
file data, which is read in by Python I/O as a string).
"""


def get_wav_file(bucket_name, key):
    # get resource
    s3 = boto3.resource('s3')

    # access bucket
    s3_object = s3.Object(bucket_name, key)
    result = s3_object.get()['Body'].read()

    return BytesIO(result)

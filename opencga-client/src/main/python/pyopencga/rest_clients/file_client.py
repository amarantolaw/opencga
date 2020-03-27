"""
WARNING: AUTOGENERATED CODE

    This code was generated by a tool.
    Autogenerated on: 2020-03-27 17:32:18
    
    Manual changes to this file may cause unexpected behavior in your application.
    Manual changes to this file will be overwritten if the code is regenerated.
"""

from pyopencga.rest_clients._parent_rest_clients import _ParentRestClient


class File(_ParentRestClient):
    """
    This class contains methods for the 'Files' webservices
    Client version: 2.0.0
    PATH: /{apiVersion}/files
    """

    def __init__(self, configuration, token=None, login_handler=None, *args, **kwargs):
        _category = 'files'
        super(File, self).__init__(configuration, _category, token, login_handler, *args, **kwargs)

    def update_acl(self, members, data=None, **options):
        """
        Update the set of permissions granted for the member.
        PATH: /{apiVersion}/files/acl/{members}/update

        :param dict data: JSON containing the parameters to add ACLs.
            (REQUIRED)
        :param str members: Comma separated list of user or group ids.
            (REQUIRED)
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        """

        return self._post('update', query_id=members, data=data, **options)

    def aggregation_stats(self, **options):
        """
        Fetch catalog file stats.
        PATH: /{apiVersion}/files/aggregationStats

        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param str name: Name.
        :param str type: Type.
        :param str format: Format.
        :param str bioformat: Bioformat.
        :param str creation_year: Creation year.
        :param str creation_month: Creation month (JANUARY, FEBRUARY...).
        :param str creation_day: Creation day.
        :param str creation_day_of_week: Creation day of week (MONDAY,
            TUESDAY...).
        :param str status: Status.
        :param str release: Release.
        :param bool external: External.
        :param str size: Size.
        :param str software: Software.
        :param str experiment: Experiment.
        :param str num_samples: Number of samples.
        :param str num_related_files: Number of related files.
        :param str annotation: Annotation, e.g: key1=value(,key2=value).
        :param bool default: Calculate default stats.
        :param str field: List of fields separated by semicolons, e.g.:
            studies;type. For nested fields use >>, e.g.:
            studies>>biotype;type;numSamples[0..10]:1.
        """

        return self._get('aggregationStats', **options)

    def load_annotation_sets(self, variable_set_id, path, data=None, **options):
        """
        Load annotation sets from a TSV file.
        PATH: /{apiVersion}/files/annotationSets/load

        :param str path: Path where the TSV file is located in OpenCGA or
            where it should be located. (REQUIRED)
        :param str variable_set_id: Variable set id or name. (REQUIRED)
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param bool parents: Flag indicating whether to create parent
            directories if they don't exist (only when TSV file was not
            previously associated).
        :param str annotation_set_id: Annotation set id. If not provided,
            variableSetId will be used.
        :param dict data: JSON containing the 'content' of the TSV file if
            this has not yet been registered into OpenCGA.
        """

        options['variable_set_id'] = variable_set_id
        options['path'] = path
        return self._post('load', subcategory='annotationSets', data=data, **options)

    def bioformats(self, **options):
        """
        List of accepted file bioformats.
        PATH: /{apiVersion}/files/bioformats
        """

        return self._get('bioformats', **options)

    def create(self, data=None, **options):
        """
        Create file or folder.
        PATH: /{apiVersion}/files/create

        :param dict data: File parameters. (REQUIRED)
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        """

        return self._post('create', data=data, **options)

    def fetch(self, data=None, **options):
        """
        Download an external file to catalog and register it.
        PATH: /{apiVersion}/files/fetch

        :param dict data: Fetch parameters. (REQUIRED)
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        """

        return self._post('fetch', data=data, **options)

    def formats(self, **options):
        """
        List of accepted file formats.
        PATH: /{apiVersion}/files/formats
        """

        return self._get('formats', **options)

    def link(self, data=None, **options):
        """
        Link an external file into catalog.
        PATH: /{apiVersion}/files/link

        :param dict data: File parameters. (REQUIRED)
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param bool parents: Create the parent directories if they do not
            exist.
        """

        return self._post('link', data=data, **options)

    def search(self, **options):
        """
        File search method.
        PATH: /{apiVersion}/files/search

        :param str include: Fields included in the response, whole JSON path
            must be provided.
        :param str exclude: Fields excluded in the response, whole JSON path
            must be provided.
        :param int limit: Number of results to be returned.
        :param int skip: Number of results to skip.
        :param bool count: Get the total number of results matching the query.
            Deactivated by default.
        :param bool lazy: False to return entire job and experiment object.
        :param bool flatten_annotations: Flatten the annotations?.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param str name: Comma separated list of file names.
        :param str path: Comma separated list of paths.
        :param str type: File type, either FILE or DIRECTORY.
        :param str bioformat: Comma separated Bioformat values. For existing
            Bioformats see files/bioformats.
        :param str format: Comma separated Format values. For existing Formats
            see files/formats.
        :param str status: File status.
        :param str directory: Directory under which we want to look for files
            or folders.
        :param str creation_date: Creation date. Format: yyyyMMddHHmmss.
            Examples: >2018, 2017-2018, <201805.
        :param str modification_date: Modification date. Format:
            yyyyMMddHHmmss. Examples: >2018, 2017-2018, <201805.
        :param str description: Description.
        :param str tags: Tags.
        :param str size: File size.
        :param str samples: Comma separated list sample IDs or UUIDs up to a
            maximum of 100.
        :param str job.id: Job id that created the file(s) or folder(s).
        :param str annotation: Annotation, e.g: key1=value(,key2=value).
        :param str acl: Filter entries for which a user has the provided
            permissions. Format: acl={user}:{permissions}. Example:
            acl=john:WRITE,WRITE_ANNOTATIONS will return all entries for which
            user john has both WRITE and WRITE_ANNOTATIONS permissions. Only
            study owners or administrators can query by this field. .
        :param bool deleted: Boolean to retrieve deleted files.
        :param str attributes: Text attributes (Format: sex=male,age>20 ...).
        :param str nattributes: Numerical attributes (Format: sex=male,age>20
            ...).
        :param str release: Release value.
        """

        return self._get('search', **options)

    def upload(self, **options):
        """
        Resource to upload a file by chunks.
        PATH: /{apiVersion}/files/upload
        """

        return self._post('upload', **options)

    def acl(self, files, **options):
        """
        Return the acl defined for the file or folder. If member is provided,
            it will only return the acl for the member.
        PATH: /{apiVersion}/files/{files}/acl

        :param str files: Comma separated list of file ids or names up to a
            maximum of 100. (REQUIRED)
        :param str study: Comma separated list of Studies
            [[user@]project:]study where study and project can be either the ID
            or UUID up to a maximum of 100.
        :param str member: User or group id.
        :param bool silent: Boolean to retrieve all possible entries that are
            queried for, false to raise an exception whenever one of the
            entries looked for cannot be shown for whichever reason.
        """

        return self._get('acl', query_id=files, **options)

    def delete(self, files, **options):
        """
        Delete existing files and folders.
        PATH: /{apiVersion}/files/{files}/delete

        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param str files: Comma separated list of file ids, names or paths.
        :param bool skip_trash: Skip trash and delete the files/folders from
            disk directly (CANNOT BE RECOVERED).
        """

        return self._delete('delete', query_id=files, **options)

    def info(self, files, **options):
        """
        File info.
        PATH: /{apiVersion}/files/{files}/info

        :param str include: Fields included in the response, whole JSON path
            must be provided.
        :param str exclude: Fields excluded in the response, whole JSON path
            must be provided.
        :param bool lazy: False to return entire job and experiment object.
        :param bool flatten_annotations: Flatten the annotations?.
        :param str files: Comma separated list of file ids or names up to a
            maximum of 100.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param bool deleted: Boolean to retrieve deleted files.
        """

        return self._get('info', query_id=files, **options)

    def unlink(self, files, **options):
        """
        Unlink linked files and folders.
        PATH: /{apiVersion}/files/{files}/unlink

        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param str files: Comma separated list of file ids, names or paths.
        """

        return self._delete('unlink', query_id=files, **options)

    def update(self, files, data=None, **options):
        """
        Update some file attributes.
        PATH: /{apiVersion}/files/{files}/update

        :param dict data: Parameters to modify. (REQUIRED)
        :param str files: Comma separated list of file ids, names or paths.
            Paths must be separated by : instead of /.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param str samples_action: Action to be performed if the array of
            samples is being updated. Allowed values: ['ADD', 'SET', 'REMOVE']
        :param str annotation_sets_action: Action to be performed if the array
            of annotationSets is being updated. Allowed values: ['ADD', 'SET',
            'REMOVE']
        :param str related_files_action: Action to be performed if the array
            of relatedFiles is being updated. Allowed values: ['ADD', 'SET',
            'REMOVE']
        :param str tags_action: Action to be performed if the array of tags is
            being updated. Allowed values: ['ADD', 'SET', 'REMOVE']
        """

        return self._post('update', query_id=files, data=data, **options)

    def update_annotations(self, file, annotation_set, data=None, **options):
        """
        Update annotations from an annotationSet.
        PATH: /{apiVersion}/files/{file}/annotationSets/{annotationSet}/annotations/update

        :param str file: File id, name or path. Paths must be separated by :
            instead of /. (REQUIRED)
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param str annotation_set: AnnotationSet id to be updated.
        :param str action: Action to be performed: ADD to add new annotations;
            REPLACE to replace the value of an already existing annotation; SET
            to set the new list of annotations removing any possible old
            annotations; REMOVE to remove some annotations; RESET to set some
            annotations to the default value configured in the corresponding
            variables of the VariableSet if any. Allowed values: ['ADD', 'SET',
            'REMOVE']
        :param dict data: Json containing the map of annotations when the
            action is ADD, SET or REPLACE, a json with only the key 'remove'
            containing the comma separated variables to be removed as a value
            when the action is REMOVE or a json with only the key 'reset'
            containing the comma separated variables that will be set to the
            default value when the action is RESET.
        """

        return self._post('annotations/update', query_id=file, subcategory='annotationSets', second_query_id=annotation_set, data=data, **options)

    def download(self, file, **options):
        """
        Download file.
        PATH: /{apiVersion}/files/{file}/download

        :param str file: File id, name or path. Paths must be separated by :
            instead of /.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        """

        return self._get('download', query_id=file, **options)

    def grep(self, file, **options):
        """
        Filter lines of the file containing the pattern.
        PATH: /{apiVersion}/files/{file}/grep

        :param str file: File uuid, id, or name.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param str pattern: String pattern.
        :param bool ignore_case: Flag to perform a case insensitive search.
        :param int max_count: Stop reading a file after 'n' matching lines. 0
            means no limit.
        """

        return self._get('grep', query_id=file, **options)

    def head(self, file, **options):
        """
        Show the first lines of a file (up to a limit).
        PATH: /{apiVersion}/files/{file}/head

        :param str file: File uuid, id, or name.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param int offset: Starting byte from which the file will be read.
        :param int lines: Maximum number of lines to be returned.
        """

        return self._get('head', query_id=file, **options)

    def refresh(self, file, **options):
        """
        Refresh metadata from the selected file or folder. Return updated
            files.
        PATH: /{apiVersion}/files/{file}/refresh

        :param str file: File id, name or path. Paths must be separated by :
            instead of /.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        """

        return self._get('refresh', query_id=file, **options)

    def tail(self, file, **options):
        """
        Show the last lines of a file (up to a limit).
        PATH: /{apiVersion}/files/{file}/tail

        :param str file: File uuid, id, or name.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param int lines: Maximum number of lines to be returned.
        """

        return self._get('tail', query_id=file, **options)

    def list(self, folder, **options):
        """
        List all the files inside the folder.
        PATH: /{apiVersion}/files/{folder}/list

        :param str include: Fields included in the response, whole JSON path
            must be provided.
        :param str exclude: Fields excluded in the response, whole JSON path
            must be provided.
        :param int limit: Number of results to be returned.
        :param int skip: Number of results to skip.
        :param bool count: Get the total number of results matching the query.
            Deactivated by default.
        :param str folder: Folder id, name or path.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        """

        return self._get('list', query_id=folder, **options)

    def tree(self, folder, **options):
        """
        Obtain a tree view of the files and folders within a folder.
        PATH: /{apiVersion}/files/{folder}/tree

        :param str include: Fields included in the response, whole JSON path
            must be provided.
        :param str exclude: Fields excluded in the response, whole JSON path
            must be provided.
        :param int limit: [PENDING] Number of results to be returned.
        :param str folder: Folder id, name or path. Paths must be separated by
            : instead of /.
        :param str study: Study [[user@]project:]study where study and project
            can be either the ID or UUID.
        :param int max_depth: Maximum depth to get files from.
        """

        return self._get('tree', query_id=folder, **options)


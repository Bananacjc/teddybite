import React, { useEffect, useState } from 'react';
import {
  Box, Heading, Button,
  Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody, ModalCloseButton,
  FormControl, FormLabel, Input, Select, useDisclosure, useToast,
  FormErrorMessage, IconButton, Checkbox,
  Table, Thead, Tbody, Tr, Th, Td, HStack, SimpleGrid, Stack,
  AlertDialog, AlertDialogBody, AlertDialogFooter, AlertDialogHeader, AlertDialogContent, AlertDialogOverlay, Text, Image
} from '@chakra-ui/react';
import { AddIcon, EditIcon, DeleteIcon, SearchIcon, TriangleDownIcon, TriangleUpIcon, CloseIcon, AttachmentIcon } from '@chakra-ui/icons';
import { getAllItems, createItem, updateItem, deleteItem, deleteItems, getItemCategories } from '../../api/items';



const ItemManagement = () => {
  const [items, setItems] = useState([]);
  const [categories, setCategories] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();

  const [errors, setErrors] = useState({});
  const [isEditMode, setIsEditMode] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // Search and Sort State
  const [filters, setFilters] = useState({
    itemName: '',
    itemCategory: '',
    itemPrice: ''
  });
  const [activeSearches, setActiveSearches] = useState({});
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'ascending' });

  // Delete Dialog State
  const { isOpen: isDeleteOpen, onOpen: onDeleteOpen, onClose: onDeleteClose } = useDisclosure();
  const [deleteType, setDeleteType] = useState(null); // 'single' or 'batch'
  const [deleteId, setDeleteId] = useState(null);
  const cancelRef = React.useRef();

  // Selection State
  const [selectedIds, setSelectedIds] = useState([]);

  // Form State
  const [formData, setFormData] = useState({
    itemName: '',
    itemCategory: '',
    itemPrice: '',
    itemImage: '',
    imageFile: null
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [itemsData, categoriesData] = await Promise.all([getAllItems(), getItemCategories()]);
      setItems(itemsData);
      setCategories(categoriesData);
    } catch (error) {
      toast({
        title: "Error fetching data",
        description: error.message,
        status: "error",
        duration: 3000,
        isClosable: true,
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const fileInputRef = React.useRef();
  const [isDragOver, setIsDragOver] = useState(false);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFormData(prev => ({ ...prev, imageFile: file }));
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    setIsDragOver(true);
  };

  const handleDragLeave = () => {
    setIsDragOver(false);
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setIsDragOver(false);
    const files = e.dataTransfer.files;
    if (files && files.length > 0) {
      setFormData(prev => ({ ...prev, imageFile: files[0] }));
    }
  };

  const handleOpenAdd = () => {
    setIsEditMode(false);
    setEditingId(null);
    setFormData({
      itemName: '',
      itemCategory: '',
      itemPrice: '',
      itemImage: '',
      imageFile: null
    });
    setErrors({});
    onOpen();
  };

  const handleEdit = (item) => {
    setIsEditMode(true);
    setEditingId(item.itemId);
    setFormData({
      itemName: item.itemName,
      itemCategory: item.itemCategory,
      itemPrice: item.itemPrice,
      itemImage: item.itemImage || '',
      imageFile: null
    });
    setErrors({});
    onOpen();
  };

  const handleSubmit = async () => {
    const newErrors = {};

    if (!formData.itemName) newErrors.itemName = "Item Name is required";
    if (!formData.itemCategory) newErrors.itemCategory = "Category is required";
    if (!formData.itemPrice) newErrors.itemPrice = "Price is required";

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setErrors({});

    try {
      const payload = {
        itemName: formData.itemName,
        itemCategory: formData.itemCategory,
        itemPrice: parseFloat(formData.itemPrice),
        itemImage: formData.itemImage,
        imageFile: formData.imageFile
      };

      if (isEditMode) {
        await updateItem(editingId, payload);
        toast({ title: "Item updated", status: "success", duration: 3000 });
      } else {
        await createItem(payload);
        toast({ title: "Item created", status: "success", duration: 3000 });
      }

      onClose();
      fetchData();

    } catch (error) {
      console.error("Error submitting item:", error);
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data); // Assuming backend sends field errors
      } else {
        toast({
          title: isEditMode ? "Failed to update" : "Failed to create",
          description: "Something went wrong",
          status: "error",
          duration: 3000,
        });
      }
    }
  };

  const handleDelete = (id) => {
    setDeleteType('single');
    setDeleteId(id);
    onDeleteOpen();
  };

  const handleBatchDelete = () => {
    setDeleteType('batch');
    onDeleteOpen();
  };

  const confirmDelete = async () => {
    onDeleteClose();
    try {
      if (deleteType === 'single') {
        await deleteItem(deleteId);
        toast({ title: "Item deleted", status: "success" });
      } else if (deleteType === 'batch') {
        await deleteItems(selectedIds);
        toast({ title: "Items deleted", status: "success" });
        setSelectedIds([]);
      }
      fetchData();
    } catch (error) {
      toast({ title: "Delete failed", status: "error" });
    }
  };

  const handleSelectAll = (e) => {
    if (e.target.checked) {
      const allIds = filteredAndSortedItems.map(item => item.itemId);
      setSelectedIds(allIds);
    } else {
      setSelectedIds([]);
    }
  };

  const handleSelectRow = (id) => {
    if (selectedIds.includes(id)) {
      setSelectedIds(selectedIds.filter(sid => sid !== id));
    } else {
      setSelectedIds([...selectedIds, id]);
    }
  };

  const handleSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });
  };

  const handleFilterChange = (key, value) => {
    setFilters(prev => ({ ...prev, [key]: value }));
  };

  const toggleSearch = (columnKey, isOpen) => {
    setActiveSearches(prev => ({ ...prev, [columnKey]: isOpen }));
    if (!isOpen) {
      handleFilterChange(columnKey, '');
    }
  };

  const filteredAndSortedItems = React.useMemo(() => {
    let processedItems = [...items];

    // Apply Filters
    Object.keys(filters).forEach(key => {
      const filterValue = filters[key].toLowerCase();
      if (filterValue) {
        processedItems = processedItems.filter(item => {
          let cellValue = item[key];

          if (cellValue) {
            return cellValue.toString().toLowerCase().includes(filterValue);
          }
          return false;
        });
      }
    });

    // Sort items
    if (sortConfig.key !== null) {
      processedItems.sort((a, b) => {
        let valA = a[sortConfig.key];
        let valB = b[sortConfig.key];

        if (valA === null || valA === undefined) valA = '';
        if (valB === null || valB === undefined) valB = '';

        if (typeof valA === 'string') valA = valA.toLowerCase();
        if (typeof valB === 'string') valB = valB.toLowerCase();

        if (valA < valB) {
          return sortConfig.direction === 'ascending' ? -1 : 1;
        }
        if (valA > valB) {
          return sortConfig.direction === 'ascending' ? 1 : -1;
        }
        return 0;
      });
    }
    return processedItems;
  }, [items, sortConfig, filters]);

  const getSortIcon = (columnName) => {
    if (sortConfig.key !== columnName) {
      return <TriangleDownIcon ml={1} w={3} h={3} color="gray.300" />;
    }
    return sortConfig.direction === 'ascending' ?
      <TriangleUpIcon ml={1} w={3} h={3} color="brand.600" /> :
      <TriangleDownIcon ml={1} w={3} h={3} color="brand.600" />;
  };

  const HeaderCell = ({ label, columnKey, width }) => {
    const isSearchOpen = activeSearches[columnKey];

    return (
      <Th width={width} verticalAlign="top" py={2}>
        <Box>
          <HStack justify="space-between" width="100%" mb={isSearchOpen ? 2 : 0}>
            <Box
              cursor="pointer"
              onClick={() => handleSort(columnKey)}
              display="flex"
              alignItems="center"
              _hover={{ color: "brand.600" }}
              flex={1}
            >
              {label} {getSortIcon(columnKey)}
            </Box>
            <IconButton
              aria-label="Search"
              icon={isSearchOpen ? <CloseIcon /> : <SearchIcon />}
              size="xs"
              variant="ghost"
              color={isSearchOpen ? "red.400" : "gray.400"}
              _hover={{ color: isSearchOpen ? "red.500" : "brand.500" }}
              onClick={() => toggleSearch(columnKey, !isSearchOpen)}
            />
          </HStack>

          {isSearchOpen && (
            <Input
              size="sm"
              autoFocus
              placeholder={`Search...`}
              value={filters[columnKey] || ''}
              onChange={(e) => handleFilterChange(columnKey, e.target.value)}
              bg="white"
              borderColor="gray.200"
              _focus={{ borderColor: "brand.500", boxShadow: "none" }}
            />
          )}
        </Box>
      </Th>
    );
  };

  return (
    <Box>
      <Stack direction={{ base: 'column', sm: 'row' }} justify="space-between" mb={6} spacing={4}>
        <Heading size="lg" color="brown.900">Item Management</Heading>
        <Stack direction={{ base: 'column', sm: 'row' }} spacing={2}>
          {selectedIds.length > 0 && (
            <Button leftIcon={<DeleteIcon />} colorScheme="red" variant="outline" onClick={handleBatchDelete} w={{ base: 'full', sm: 'auto' }}>
              Delete Selected ({selectedIds.length})
            </Button>
          )}
          <Button leftIcon={<AddIcon />} colorScheme="brand" bg="brand.500" color="brown.900" onClick={handleOpenAdd} w={{ base: 'full', sm: 'auto' }}>
            Add New Item
          </Button>
        </Stack>
      </Stack>

      <Box bg="white" borderRadius="xl" boxShadow="sm" p={4}>
        <Box overflowX="auto">
          <Table variant="simple">
            <Thead>
              <Tr>
                <Th width="40px" px={2}>
                  <Checkbox
                    isChecked={selectedIds.length === filteredAndSortedItems.length && filteredAndSortedItems.length > 0}
                    isIndeterminate={selectedIds.length > 0 && selectedIds.length < filteredAndSortedItems.length}
                    onChange={handleSelectAll}
                    colorScheme="brand"
                  />
                </Th>
                <Th width="80px">Image</Th>
                <HeaderCell label="ID" columnKey="itemId" width="100px" />
                <HeaderCell label="Name" columnKey="itemName" width="200px" />
                <HeaderCell label="Category" columnKey="itemCategory" width="150px" />
                <HeaderCell label="Price (RM)" columnKey="itemPrice" width="120px" />
                <Th pt={3} width="100px">Action</Th>
              </Tr>
            </Thead>
            <Tbody>
              {filteredAndSortedItems.map((item) => (
                <Tr key={item.itemId}>
                  <Td px={2}>
                    <Checkbox
                      isChecked={selectedIds.includes(item.itemId)}
                      onChange={() => handleSelectRow(item.itemId)}
                      colorScheme="brand"
                    />
                  </Td>
                  <Td>
                    {item.itemImage ? (
                      <Image boxSize="50px" objectFit="cover" borderRadius="md" src={item.itemImage} alt={item.itemName} fallbackSrc="https://via.placeholder.com/50" />
                    ) : (
                      <Box boxSize="50px" bg="gray.100" borderRadius="md" />
                    )}
                  </Td>
                  <Td fontWeight="medium" fontSize="sm" color="gray.500">{item.itemId}</Td>
                  <Td fontWeight="medium">{item.itemName}</Td>
                  <Td>{item.itemCategory}</Td>
                  <Td isNumeric>RM {item.itemPrice?.toFixed(2)}</Td>
                  <Td>
                    <HStack spacing={2}>
                      <Button size="sm" colorScheme="blue" variant="ghost" onClick={() => handleEdit(item)}>
                        <EditIcon />
                      </Button>
                      <Button size="sm" colorScheme="red" variant="ghost" onClick={() => handleDelete(item.itemId)}>
                        <DeleteIcon />
                      </Button>
                    </HStack>
                  </Td>
                </Tr>
              ))}
              {filteredAndSortedItems.length === 0 && !isLoading && (
                <Tr>
                  <Td colSpan={7} textAlign="center" py={4}>No items found matching your filters.</Td>
                </Tr>
              )}
            </Tbody>
          </Table>
        </Box>
      </Box>

      {/* Add/Edit Modal */}
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>{isEditMode ? "Edit Item" : "Add New Item"}</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <SimpleGrid columns={1} spacing={4}>
              <FormControl isRequired isInvalid={!!errors.itemName}>
                <FormLabel>Item Name</FormLabel>
                <Input name="itemName" value={formData.itemName} onChange={handleInputChange} />
                <FormErrorMessage>{errors.itemName}</FormErrorMessage>
              </FormControl>

              <FormControl isRequired isInvalid={!!errors.itemCategory}>
                <FormLabel>Category</FormLabel>
                <Select placeholder='Select Category' name="itemCategory" value={formData.itemCategory} onChange={handleInputChange}>
                  {categories.map(cat => (
                    <option key={cat} value={cat}>{cat.replace('_', ' ')}</option>
                  ))}
                </Select>
                <FormErrorMessage>{errors.itemCategory}</FormErrorMessage>
              </FormControl>

              <FormControl isRequired isInvalid={!!errors.itemPrice}>
                <FormLabel>Price</FormLabel>
                <Input name="itemPrice" type="number" step="0.01" value={formData.itemPrice} onChange={handleInputChange} />
                <FormErrorMessage>{errors.itemPrice}</FormErrorMessage>
              </FormControl>

              <FormControl>
                <FormLabel>Item Image</FormLabel>
                {formData.itemImage && !formData.imageFile && (
                  <Box mb={2}>
                    <Image src={formData.itemImage} alt="Current" boxSize="100px" objectFit="cover" borderRadius="md" />
                    <Text fontSize="xs" color="gray.500">Current Image</Text>
                  </Box>
                )}
                <Input
                  type="file"
                  accept="image/*"
                  ref={fileInputRef}
                  hidden
                  onChange={handleFileChange}
                />
                <Box
                  border="2px dashed"
                  borderColor={isDragOver ? "brand.500" : "gray.300"}
                  borderRadius="md"
                  p={6}
                  textAlign="center"
                  cursor="pointer"
                  onDragOver={handleDragOver}
                  onDragLeave={handleDragLeave}
                  onDrop={handleDrop}
                  onClick={() => fileInputRef.current.click()}
                  bg={isDragOver ? "gray.50" : "white"}
                  transition="all 0.2s"
                  _hover={{ borderColor: "brand.500", bg: "gray.50" }}
                >
                  <AttachmentIcon w={8} h={8} color="gray.400" mb={2} />
                  <Text color="gray.500">
                    {formData.imageFile ? formData.imageFile.name : "Drag 'n' drop image here, or click to select"}
                  </Text>
                </Box>
              </FormControl>
            </SimpleGrid>
          </ModalBody>

          <ModalFooter>
            <Button variant="ghost" mr={3} onClick={onClose}>Cancel</Button>
            <Button colorScheme="brand" bg="brand.500" color="brown.900" onClick={handleSubmit}>
              {isEditMode ? "Update Item" : "Save Item"}
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* Delete Confirmation Dialog */}
      <AlertDialog
        isOpen={isDeleteOpen}
        leastDestructiveRef={cancelRef}
        onClose={onDeleteClose}
      >
        <AlertDialogOverlay>
          <AlertDialogContent>
            <AlertDialogHeader fontSize="lg" fontWeight="bold">
              Delete Item{deleteType === 'batch' ? 's' : ''}
            </AlertDialogHeader>

            <AlertDialogBody>
              Are you sure? You can't undo this action afterwards.
              {deleteType === 'batch' && (
                <Text mt={2} fontWeight="bold">
                  You are about to delete {selectedIds.length} items.
                </Text>
              )}
            </AlertDialogBody>

            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onDeleteClose}>
                Cancel
              </Button>
              <Button colorScheme="red" onClick={confirmDelete} ml={3}>
                Delete
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </Box>
  );
};

export default ItemManagement;

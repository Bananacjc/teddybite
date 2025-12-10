import React, { useEffect, useState } from 'react';
import {
  Box, Heading, Button,
  Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody, ModalCloseButton,
  FormControl, FormLabel, Input, Select, useDisclosure, useToast,
  FormErrorMessage, IconButton, Checkbox,
  Table, Thead, Tbody, Tr, Th, Td, HStack, SimpleGrid, Stack,
  AlertDialog, AlertDialogBody, AlertDialogFooter, AlertDialogHeader, AlertDialogContent, AlertDialogOverlay, Text,
  Spinner
} from '@chakra-ui/react';
import { AddIcon, EditIcon, DeleteIcon, SearchIcon, TriangleDownIcon, TriangleUpIcon, CloseIcon, ViewIcon } from '@chakra-ui/icons';
import { getAllPayments, createPayment, updatePayment, deletePayment, deletePayments } from '../../api/payments';

const PaymentManagement = () => {
  const [payments, setPayments] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();

  const [errors, setErrors] = useState({});
  const [isEditMode, setIsEditMode] = useState(false);
  const [editingId, setEditingId] = useState(null);

  // Search and Sort State
  const [filters, setFilters] = useState({
    paymentId: '',
    paymentAmount: '',
    paymentType: ''
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
    paymentAmount: '',
    paymentType: ''
  });

  const PAYMENT_TYPES = ['CASH', 'CREDIT_CARD'];

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const data = await getAllPayments();
      setPayments(data);
    } catch (error) {
      toast({
        title: "Error fetching payments",
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

  const handleOpenAdd = () => {
    setIsEditMode(false);
    setEditingId(null);
    setFormData({
      paymentAmount: '',
      paymentType: ''
    });
    setErrors({});
    onOpen();
  };

  const handleEdit = (payment) => {
    setIsEditMode(true);
    setEditingId(payment.paymentId);
    setFormData({
      paymentAmount: payment.paymentAmount,
      paymentType: payment.paymentType || ''
    });
    setErrors({});
    onOpen();
  };

  const handleSubmit = async () => {
    const newErrors = {};

    if (!formData.paymentAmount) newErrors.paymentAmount = "Amount is required";
    if (!formData.paymentType) newErrors.paymentType = "Payment Type is required";

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setErrors({});

    try {
      const payload = {
        paymentAmount: parseFloat(formData.paymentAmount),
        paymentType: formData.paymentType
      };

      if (isEditMode) {
        await updatePayment(editingId, payload);
        toast({ title: "Payment updated", status: "success", duration: 3000 });
      } else {
        await createPayment(payload);
        toast({ title: "Payment created", status: "success", duration: 3000 });
      }

      onClose();
      fetchData();

    } catch (error) {
      console.error("Error submitting payment:", error);
      if (error.response && error.response.status === 400) {
        setErrors(error.response.data);
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
        await deletePayment(deleteId);
        toast({ title: "Payment deleted", status: "success" });
      } else if (deleteType === 'batch') {
        await deletePayments(selectedIds);
        toast({ title: "Payments deleted", status: "success" });
        setSelectedIds([]);
      }
      fetchData();
    } catch (error) {
      toast({ title: "Delete failed", status: "error" });
    }
  };

  const handleSelectAll = (e) => {
    if (e.target.checked) {
      const allIds = filteredAndSortedPayments.map(p => p.paymentId);
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

  const filteredAndSortedPayments = React.useMemo(() => {
    let processed = [...payments];

    // Filter
    Object.keys(filters).forEach(key => {
      const filterValue = filters[key].toLowerCase();
      if (filterValue) {
        processed = processed.filter(item => {
          let cellValue = item[key];
          if (cellValue) {
            return cellValue.toString().toLowerCase().includes(filterValue);
          }
          return false;
        });
      }
    });

    // Sort
    if (sortConfig.key !== null) {
      processed.sort((a, b) => {
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
    return processed;
  }, [payments, sortConfig, filters]);

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
        <Heading size="lg" color="brown.900">Payment Management</Heading>
        <Stack direction={{ base: 'column', sm: 'row' }} spacing={2}>
          {selectedIds.length > 0 && (
            <Button leftIcon={<DeleteIcon />} colorScheme="red" variant="outline" onClick={handleBatchDelete} w={{ base: 'full', sm: 'auto' }}>
              Delete Selected ({selectedIds.length})
            </Button>
          )}
          <Button leftIcon={<AddIcon />} colorScheme="brand" bg="brand.500" color="brown.900" onClick={handleOpenAdd} w={{ base: 'full', sm: 'auto' }}>
            Add New Payment
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
                    isChecked={selectedIds.length === filteredAndSortedPayments.length && filteredAndSortedPayments.length > 0}
                    isIndeterminate={selectedIds.length > 0 && selectedIds.length < filteredAndSortedPayments.length}
                    onChange={handleSelectAll}
                    colorScheme="brand"
                  />
                </Th>
                <HeaderCell label="Payment ID" columnKey="paymentId" width="200px" />
                <HeaderCell label="Amount (RM)" columnKey="paymentAmount" width="150px" />
                <HeaderCell label="Payment Type" columnKey="paymentType" width="150px" />
                <Th pt={3} width="100px">Action</Th>
              </Tr>
            </Thead>
            <Tbody>
              {isLoading ? (
                <Tr>
                  <Td colSpan={5} textAlign="center" py={10}>
                    <Spinner color="brand.500" />
                  </Td>
                </Tr>
              ) : filteredAndSortedPayments.map((payment) => (
                <Tr key={payment.paymentId}>
                  <Td px={2}>
                    <Checkbox
                      isChecked={selectedIds.includes(payment.paymentId)}
                      onChange={() => handleSelectRow(payment.paymentId)}
                      colorScheme="brand"
                    />
                  </Td>
                  <Td fontWeight="medium" fontSize="sm">{payment.paymentId}</Td>
                  <Td isNumeric>RM {payment.paymentAmount.toFixed(2)}</Td>
                  <Td>
                    <Text
                      as="span"
                      px={2}
                      py={1}
                      borderRadius="full"
                      bg={payment.paymentType === 'CASH' ? 'green.100' : 'blue.100'}
                      color={payment.paymentType === 'CASH' ? 'green.800' : 'blue.800'}
                      fontSize="xs"
                      fontWeight="bold"
                    >
                      {payment.paymentType}
                    </Text>
                  </Td>
                  <Td>
                    <HStack spacing={2}>
                      <Button size="sm" colorScheme="blue" variant="ghost" onClick={() => handleEdit(payment)}>
                        <EditIcon />
                      </Button>
                      <Button size="sm" colorScheme="red" variant="ghost" onClick={() => handleDelete(payment.paymentId)}>
                        <DeleteIcon />
                      </Button>
                    </HStack>
                  </Td>
                </Tr>
              ))}
              {filteredAndSortedPayments.length === 0 && !isLoading && (
                <Tr>
                  <Td colSpan={5} textAlign="center" py={4}>No payments found matching your filters.</Td>
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
          <ModalHeader>{isEditMode ? "Edit Payment" : "Add New Payment"}</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <SimpleGrid columns={1} spacing={4}>
              <FormControl isRequired isInvalid={!!errors.paymentAmount}>
                <FormLabel>Amount (RM)</FormLabel>
                <Input name="paymentAmount" type="number" step="0.01" value={formData.paymentAmount} onChange={handleInputChange} />
                <FormErrorMessage>{errors.paymentAmount}</FormErrorMessage>
              </FormControl>

              <FormControl isRequired isInvalid={!!errors.paymentType}>
                <FormLabel>Payment Type</FormLabel>
                <Select placeholder='Select Type' name="paymentType" value={formData.paymentType} onChange={handleInputChange}>
                  {PAYMENT_TYPES.map(type => (
                    <option key={type} value={type}>{type}</option>
                  ))}
                </Select>
                <FormErrorMessage>{errors.paymentType}</FormErrorMessage>
              </FormControl>
            </SimpleGrid>
          </ModalBody>

          <ModalFooter>
            <Button variant="ghost" mr={3} onClick={onClose}>Cancel</Button>
            <Button colorScheme="brand" bg="brand.500" color="brown.900" onClick={handleSubmit}>
              {isEditMode ? "Update Payment" : "Save Payment"}
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
              Delete Payment{deleteType === 'batch' ? 's' : ''}
            </AlertDialogHeader>

            <AlertDialogBody>
              Are you sure? This will remove the transaction record.
              {deleteType === 'batch' && (
                <Text mt={2} fontWeight="bold">
                  You are about to delete {selectedIds.length} payments.
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

export default PaymentManagement;
